package com.search2sql.impl.translator;

import com.search2sql.impl.translator.util.SqlPropertiesLoader;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.translator.Translator;

import java.util.Properties;

/**
 * This is a better implementation of {@link Translator}.
 * <br />
 * The <code>FileTranslator</code> is a more advanced Translator than the deprecated
 * {@link com.search2sql.impl.interpreter.BasicInterpreter BasicInterpreter}.
 * <br /><br />
 * This class translates the list of {@link SubQuery SubQueries} (wrapped in a {@link Query}) into sql. While doing this
 * it reads how to translate those pieces into SQL from a file instead of hard-coding them. This also enables the simpler
 * creation of custom <code>Parsers</code> because the user can add his own SQL pieces without creating an own translator.
 * Additionally, this allows to change the given pieces (e. g. another SQL dialect) because the user can create an own
 * <code>.properties</code> file that overwrites the provided pieces.
 */
public class FileTranslator extends Translator {

    private final Properties props;

    /**
     * This constructor initializes an {@link Properties} object based on the <code>sql.properties</code> file.
     * This file will be used to load all pieces that help to translate the {@link Query} to an SQL string.
     * <br /><br />
     * <b>See Also</b><br />
     * {@link FileTranslator#FileTranslator(String)} (defines a custom <code>.properties</code> file)
     */
    public FileTranslator() {
        // load base properties
        props = SqlPropertiesLoader.getProperties("sql.properties");
    }

    /**
     * This constructor does the same as {@link FileTranslator#FileTranslator()} (loads properties from <code>sql.properties</code>
     * file). As an addition this constructor takes the path to another custom <code>.properties</code> file and the basic
     * values with its values. Basically, this adds or overwrites (if existing) the key-value-pairs
     *
     * @param customProperties
     */
    public FileTranslator(String customProperties) {
        // load base properties
        props = SqlPropertiesLoader.getProperties("sql.properties");

        /*
        add all custom properties defined in the user's properties file
        if a value already exists it will be overwritten
         */
        props.putAll(SqlPropertiesLoader.getProperties(customProperties));
    }

    /**
     * This method translates the given {@link SubQuery SubQueries} (wrapped in a {@link Query}) and translate them to
     * SQL. This method will return a SQL-Injection safe string because the values are replaced with <code>?</code> (question
     * mark's. Therefore this string should be used with a {@link java.sql.PreparedStatement PreparedStatement}. For more
     * information see {@link Translator} or the documentation.
     *
     * @param query parsed & interpreted version of the basic string expression
     * @return translated sql string
     */
    @Override
    public String translate(Query query) {
        // initializes a new StringBuilder to save the sql
        StringBuilder sql = new StringBuilder();

        // iterate over every SubQuery
        for (SubQuery subQuery : query.getSubQueries()) {

            // resolves the property key and loads the value
            String property = (String) props.get(resolvePropertyKey(subQuery.getParserId(), subQuery.getType()));

            // checks if property exists
            if (property != null) {
                // adds property value with $ replaced with the current column
                sql.append(property.trim().replaceAll("\\$", subQuery.getColumnName()));

                // appends a whitespace
                sql.append(" ");
            } else {
                // TODO better handling for missing value
            }
        }

        // removes last whitespace and returns the full sql string
        return sql.toString().trim();
    }

    /**
     * This method resolves the property key to load the value for the current SearchQuery.<br />
     * The key consists of following pieces:<br />
     * <code>parserId.type(.subType)</code><br />
     * <code>subType</code> is not an attribute of SearchQuery. The subType is saved in the <code>type</code> attribute
     * and therefore is only an utility for better grouping. There can also be multiple subTypes.<br />
     * It is also possible that the parserId value is null/empty. In this case the parserId will be ignored. As example
     * SubQueries added while interpreting (logic connectors, etc.) are permitted to not specify a parser id.<br />
     * If a parser only has one type the type attribute can be null/empty and will be ignored.
     *
     * @param parserId id of the parser
     * @param type type of the SearchQuery
     * @return property key
     */
    private String resolvePropertyKey(String parserId, String type) {
        // initialize key value
        String key = "";

        // checks if parser id is present
        if (parserId != null && !parserId.isEmpty()) {
            // adds it to the key
            key += parserId;
        }

        // checks if parser id and type are available
        if (parserId != null && type != null && !parserId.isEmpty() && !type.isEmpty()) {
            // adds delimiter
            key += ".";
        }

        // checks if type is present
        if (type != null && !type.isEmpty()) {
            // adds type to the key
            key += type;
        }

        // returns resolved key
        return key;
    }
}
