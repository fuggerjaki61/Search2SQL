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
import org.reflections8.Reflections;

import java.util.HashSet;
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
                    result.addSubQuery(new SubQuery(null, "logic.connector.or", null));
                }
            }
        }

        if (!result.getSubQueries().isEmpty()) {
            result.getSubQueries().removeLast();
        }

        // return interpreted, complete Query
        return result;
    }

    private LinkedList<String> splitQuery(String searchQuery, TableConfig config) {
        // initialize result list
        LinkedList<String> list = new LinkedList<>();

        // initialize hashset with all characters that are allowed as quotation delimiters
        HashSet<Character> quotationChars = new HashSet<>();

        // iterate over every column in config
        for (Column column : config.getColumns()) {
            // get parser for column
            Parser parser = loadParser(column);

            // check if parser is subclass of QuotedParser
            if (QuotedParser.class.isAssignableFrom(parser.getClass())) {
                QuotedParser quotedParser = (QuotedParser) parser;

                // get&add delimiter to set
                quotationChars.add(quotedParser.getQuotationChar());
            }
        }

        // flag indicating if is in a quote right now
        boolean quote = false;

        // collect the characters to a string
        StringBuilder query = new StringBuilder();

        // iterate over every char
        for (char c : searchQuery.toCharArray()) {
            // checks if character isn't any whitespace or is in a quote
            if (!Character.isWhitespace(c) || quote) {
                // append the current character to the list
                query.append(c);

                // TODO quote should be terminated by the same delimiter it was started

                // check if character is defined as delimiter
                if (quotationChars.contains(c)) {
                    /*
                     * if the quote flag is currently set to true, one is in a quote that will be terminated
                     * because another delimiter is mentioned. This quote should be added without any exception.
                     *
                     * To summarize this: this will always add the quote when the quote is terminated
                     */
                    if (quote) {
                        // does the same as normally adding the query

                        // add it to the result list
                        list.add(query.toString());

                        // remove all characters from query
                        query = new StringBuilder();
                    }

                    // inverse quote
                    quote = !quote;
                }
            } else if ((query.length() > 0)) {
                /*
                 * the current char is a whitespace so it should be added, except:
                 * - the query is empty
                 * - a query was started
                 */

                // adds query to the result list
                list.add(query.toString());

                // clears query
                query = new StringBuilder();
            }
        }

        /*
         * the last character may not be a whitespace so the last query won't be added.
         * checks if query is not empty and adds it if not
         */
        if (query.length() > 0) {
            list.add(query.toString());
        }

        return list;
    }

    private Parser loadParser(Column column) {
        // lazy load the list of parsers
        if (searchParsers == null) {
            initializeParsers();
        }

        // iterates over every class defining the @SearchParser annotation
        for (Class<?> clazz : searchParsers) {
            // finds the @SearchParser annotation
            SearchParser parserAnnotation = clazz.getAnnotation(SearchParser.class);

            // checks if the annotation parser id and the column parser id are equal
            if (parserAnnotation.value().equals(column.getParserId())) {
                // validates parser and throws RunTimeException if not
                validateParser(clazz);

                try {
                    // return a new instance
                    return (Parser) clazz.getConstructor().newInstance();
                } catch (Exception e) {
                    // there was one of many reasons why this class couldn't be initialized
                    throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation" +
                            "but it couldn't been initialized. Please check the attached error message.", clazz.getName()), e);
                }
            }
        }

        // no parser could be found, so the id may be wrong
        throw new InvalidParserException(String.format("The parser with the id '%s' couldn't be found. Please check if the id is valid.", column.getParserId()));
    }


    public static void initializeParsers() {
        // basically just searches all classes defining the @SearchParser annotation
        searchParsers = new Reflections().getTypesAnnotatedWith(SearchParser.class);
    }

    private void validateParser(Class<?> parser) {
        if (!Parser.class.isAssignableFrom(parser)) {
            /*
             * the class defines the @SearchParser annotation but does not extend the Parser class
             */
            throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation " +
                    "but isn't a (in)direct sub class of 'com.search2sql.parser.Parser'.", parser.getName()));
        }
    }
}
