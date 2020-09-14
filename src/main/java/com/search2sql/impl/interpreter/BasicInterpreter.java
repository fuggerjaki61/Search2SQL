package com.search2sql.impl.interpreter;

import com.search2sql.exception.InvalidParserException;
import com.search2sql.impl.parser.QuotedParser;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import org.reflections.Reflections;

import java.util.LinkedList;
import java.util.Set;

public class BasicInterpreter extends Interpreter {

    protected static Set<Class<?>> searchParsers;

    @Override
    public Query interpret(String searchQuery, TableConfig tableConfig) {
        // instantiate new query
        Query result = new Query(searchQuery, tableConfig);

        for (String query : splitQuery(searchQuery, tableConfig)) {
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

        result.getSubQueries().removeLast();

        // return interpreted, complete Query
        return result;
    }

    private LinkedList<String> splitQuery(String searchQuery, TableConfig config) {
        LinkedList<String> list = new LinkedList<>();

        LinkedList<Character> quotationChars = new LinkedList<>();

        for (Column column : config.getColumns()) {
            if ("quoted".equals(column.getParserId())) {
                QuotedParser parser = (QuotedParser) loadParser(column);

                quotationChars.add(parser.getQuotationChar());
            }
        }

        boolean quote = false;
        StringBuilder query = new StringBuilder();

        for (char c : searchQuery.toCharArray()) {
            if (c != ' ' || quote) {
                if (quotationChars.contains(c)) {
                    quote = !quote;
                }

                query.append(c);
            } else if ((query.length() > 0)) {
                list.add(query.toString());

                query = new StringBuilder();
            }
        }

        list.add(query.toString());

        return list;
    }

    private Parser loadParser(Column column) {
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

        throw new InvalidParserException(String.format("The parser with the id '%s' couldn't be found. Please check if the id is valid.", column.getParserId()));
    }

    public static void initializeParsers() {
        searchParsers = new Reflections().getTypesAnnotatedWith(SearchParser.class);
    }

    private void validateParser(Class<?> parser) {
        if (!Parser.class.isAssignableFrom(parser)) {
            throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation " +
                    "but isn't a (in)direct sub class of 'com.search2sql.parser.Parser'.", parser.getName()));
        }
    }
}
