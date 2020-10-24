package com.search2sql.impl.translator;

import com.search2sql.impl.translator.util.SqlPropertiesLoader;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.translator.Translator;

import java.util.Properties;

/**
 * This is a basic implementation of {@link Translator}.
 * <br /><br />
 * This implementation loads the sql piece for each {@link SubQuery} out of a file and puts the pieces together to a full
 * sql string.
 */
public class FileTranslator extends Translator {

    private final Properties properties;

    /**
     * Basic constructor that uses the {@link SqlPropertiesLoader} to initialize the Properties.
     */
    public FileTranslator() {
        // load properties
        properties = new SqlPropertiesLoader().getProperties();
    }

    /**
     *
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
            String property = (String) properties.get(resolvePropertyKey(subQuery.getParserId(), subQuery.getType()));

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
