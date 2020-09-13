package com.search2sql.impl.interpreter;

import com.search2sql.exception.InvalidParserException;
import com.search2sql.impl.interpreter.util.ParserUtils;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class BasicInterpreter extends Interpreter {

    protected static Set<Class<?>> searchParsers;

    @Override
    public Query interpret(String searchQuery, TableConfig tableConfig) {
        // instantiate new query
        Query result = new Query(searchQuery, tableConfig);

        // split by any whitespace(s) and iterate over it
        for (String query : searchQuery.split("\\s+")) {

            for (Column column : tableConfig.getColumns()) {
                Parser parser = loadParser(column);

                if (parser.isParserFor(query)) {
                    SubQuery subQuery = parser.parse(query);

                    // set the column name
                    subQuery.setColumnName(column.getName());

                    // add the query to the list
                    result.addSubQuery(subQuery);

                    // adds a logical 'OR' everytime a new sub-query was added
                    result.addSubQuery(new SubQuery(null, "logic.connector", "or"));
                }
            }
        }

        // last query must be a logic 'OR'
        // query can't end with a logic 'OR' so it will be removed
        result.getSubQueries().removeLast();

        // return interpreted, complete Query
        return result;
    }

    public static Parser loadParser(Column column) {
        if (searchParsers == null) {
            initializeParsers();
        }

        for (Class<?> clazz : searchParsers) {
            SearchParser parserAnnotation = clazz.getAnnotation(SearchParser.class);

            if (parserAnnotation.value().equals(column.getParserId())) {
                validateParser(clazz);

                try {
                    return (Parser) clazz.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation" +
                            "but it couldn't been initialized. Please check the attached error message.", clazz.getName()), e);
                }
            }
        }

        return null;
    }

    public static void initializeParsers() {
        searchParsers = new Reflections().getTypesAnnotatedWith(SearchParser.class);
    }

    private static void validateParser(Class<?> parser) {
        if (!Parser.class.isAssignableFrom(parser)) {
            throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation " +
                    "but isn't a (in)direct sub class of 'com.search2sql.parser.Parser'.", parser.getName()));
        }
    }
}
