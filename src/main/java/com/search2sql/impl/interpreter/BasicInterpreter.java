package com.search2sql.impl.interpreter;

import com.search2sql.impl.interpreter.util.ParserUtils;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.parser.Parser;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.TableConfig;

import java.util.LinkedList;

public class BasicInterpreter extends Interpreter {
    @Override
    public Query interpret(String searchQuery, TableConfig tableConfig) {
        Query result = new Query(searchQuery, tableConfig);

        LinkedList<Parser> parsers = ParserUtils.loadParser(tableConfig);

        for (String query : searchQuery.split("\\s+")) {
            for (Parser parser : parsers) {
                if (parser.isParserFor(query)) {
                    result.addSubQuery(parser.parse(query));
                }

                result.addSubQuery(new SubQuery(null, "logic.connector", "or"));
            }
        }

        result.getSubQueries().removeLast();

        return result;
    }
}
