package com.search2sql.impl.interpreter;

import com.search2sql.impl.interpreter.util.ParserUtils;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.parser.Parser;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.TableConfig;

import java.util.HashMap;
import java.util.Map;


public class BasicInterpreter extends Interpreter {
    @Override
    public Query interpret(String searchQuery, TableConfig tableConfig) {
        // instantiate new query
        Query result = new Query(searchQuery, tableConfig);

        // load map of column name and parser instance
        HashMap<String, Parser> parsers = ParserUtils.loadParser(tableConfig);

        // split by any whitespace(s) and iterate over it
        for (String query : searchQuery.split("\\s+")) {

            // iterate over every allowed parser
            for (Map.Entry<String, Parser> entry : parsers.entrySet()) {
                // checks if query is parsable by current parser
                if (entry.getValue().isParserFor(query)) {
                    // parse it
                    SubQuery subQuery = entry.getValue().parse(query);

                    // set the column name
                    subQuery.setColumnName(entry.getKey());

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
}
