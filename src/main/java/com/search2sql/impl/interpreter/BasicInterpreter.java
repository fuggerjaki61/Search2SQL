package com.search2sql.impl.interpreter;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.impl.interpreter.util.ParserLoader;
import com.search2sql.impl.parser.QuotedParser;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.parser.Parser;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.Column;
import com.search2sql.table.Table;
import com.search2sql.table.TableConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * <code>BasicInterpreter</code> is a basic implementation of {@link Interpreter}. This interpreter splits the search
 * expression and let {@link Parser Parsers} parse them.
 * <br><br>
 * This Interpreter splits the search expression by any whitespaces except the interpreter notices a quote. Quotes can
 * be started with any single char but it is recommended that it is a special character like <code>"</code> or
 * <code>'</code>. These special characters are defined over the {@link QuotedParser} and any subclass of it
 * (e.g. {@link com.search2sql.impl.parser.TextParser TextParser}). After the query was split, the generated sub-queries
 * are processed. Every parser will try to parse every sub-query. Therefore an 11 can be parsed as a number or as text.
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
public class BasicInterpreter extends Interpreter {

    /**
     * This is the implementation of the {@link Interpreter#interpret(String, TableConfig)} method. This method is
     * responsible for all actions happening that are visible. This method takes the original string search expression
     * and the {@link TableConfig} and splits the string in good editable sub-queries. These will be parsed by the responding
     * parser(s) and finally be added to the Query. After each successful parsing, a logical <code>OR</code> will be added.
     * For more information see {@link BasicInterpreter}.
     *
     * @param searchQuery simple string form of the search query
     * @param tableConfig meta-information about the table (column types, etc.)
     * @return parsed and interpreted Query
     * @throws InvalidSearchException thrown if there was a problem with the search
     */
    @Override
    public Query interpret(String searchQuery, TableConfig tableConfig) throws InvalidSearchException {
        // instantiate new query
        Query result = new Query(searchQuery, tableConfig, new LinkedList<>());

        // initialize new map with id and its loaded parsers
        // this map saves all parsers with its id so it has only to loaded once
        Map<String, Parser> parsers = new HashMap<>();

        // iterate over all tables
        for (Table table : tableConfig.getTables()) {
            // iterate over all parsers
            for (Column column : table.getColumns()) {
                // check if parser already exists
                if (!parsers.containsKey(column.getParserId())) {
                    // if not, load and add it
                    parsers.put(column.getParserId(), ParserLoader.loadParser(column.getParserId()));
                }
            }
        }

        // iterate over every split query
        for (String query : splitQuery(searchQuery, parsers, tableConfig)) {
            // a flag indicating if this query was parsed
            boolean parsed = false;

            // iterate over every column
            for (Table table : tableConfig.getTables()) {
                // iterate over every column
                for (Column column : table.getColumns()) {
                    // get the responding parser for the column
                    Parser parser = parsers.get(column.getParserId());

                    // check if the parser can parse the query
                    if (parser.isParserFor(query)) {
                        // set the flag to true
                        parsed = true;

                        // it can, so parse it
                        SubQuery subQuery = parser.parse(query);

                        // add metadata for translation
                        subQuery.setColumnName(column.getName());

                        // add the query to the list
                        result.addSubQuery(subQuery);

                        // adds a logical 'OR' everytime a new sub-query was added
                        result.addSubQuery(new SubQuery(null, "logic.connector.or", null));
                    }
                }
            }

            // check if this sub query was parsed
            if (!parsed) {
                /*
                 * No column was specified that could parse this sub query.
                 * This search expression is invalid and that is shown to the user with the error code 1.
                 */
                throw new InvalidSearchException(1);
            }
        }

        // checks if there is a result
        if (!result.getSubQueries().isEmpty()) {
            // there are results, but the last one is always an 'OR'
            result.getSubQueries().remove(result.getSubQueries().size() - 1);
        }

        // return interpreted, complete Query
        return result;
    }

    private LinkedList<String> splitQuery(String searchQuery, Map<String, Parser> parsers, TableConfig config) throws InvalidSearchException {
        // initialize result list
        LinkedList<String> list = new LinkedList<>();

        // initialize hashset with all characters that are allowed as quotation delimiters
        HashSet<Character> quotationChars = new HashSet<>();

        // iterate over every table in config
        for (Table table : config.getTables()) {
            // iterate over every column in config
            for (Column column : table.getColumns()) {
                // get parser for column
                Parser parser = parsers.get(column.getParserId());

                // check if parser is subclass of QuotedParser
                if (QuotedParser.class.isAssignableFrom(parser.getClass())) {
                    QuotedParser quotedParser = (QuotedParser) parser;

                    // get&add delimiter to set
                    quotationChars.add(quotedParser.getQuotation());
                }
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

        // checks if a quote hasn't been terminated
        if (quote) {
            /*
             * One or more quote(s) haven't been terminated.
             * This is invalid and shown to the user with the error code 2.
             */
            throw new InvalidSearchException(2);
        }

        /*
         * the last character may not be a whitespace so the last query won't be added.
         * checks if query is not empty and adds it if not
         */
        if (query.length() > 0) {
            list.add(query.toString());
        }

        // return fully split list
        return list;
    }
}
