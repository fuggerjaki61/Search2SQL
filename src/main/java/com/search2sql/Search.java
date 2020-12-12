package com.search2sql;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.exception.i18n.ExceptionHandler;
import com.search2sql.exception.i18n.LocalizedExceptionHandler;
import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.FileTranslator;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.TableConfig;
import com.search2sql.translator.Translator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Search {

    private final TableConfig tableConfig;
    private final Interpreter interpreter;
    private final Translator translator;
    private final ExceptionHandler exceptionHandler;

    public Search(TableConfig tableConfig) {
        this(tableConfig,
                new BasicInterpreter(),
                new FileTranslator(),
                new LocalizedExceptionHandler());
    }

    Search(TableConfig tableConfig, Interpreter interpreter, Translator translator, ExceptionHandler exceptionHandler) {
        this.tableConfig = tableConfig;
        this.interpreter = interpreter;
        this.translator = translator;
        this.exceptionHandler = exceptionHandler;
    }

    public PreparedStatement preparedStatement(String search, Connection connection,
                                               String queryPrefix) throws SQLException, InvalidSearchException {
        return prepareStatement(search, connection, queryPrefix, "", 1);
    }

    public PreparedStatement preparedStatement(String search, Connection connection,
                                               String queryPrefix, int startIndex) throws SQLException, InvalidSearchException {
        return prepareStatement(search, connection, queryPrefix, "", startIndex);
    }

    public PreparedStatement prepareStatement(String search, Connection connection, String queryPrefix,
                                               String querySuffix, int startIndex) throws InvalidSearchException, SQLException {
        Query query = interpreter.interpret(search, tableConfig);

        int current = startIndex;
        PreparedStatement ps = connection.prepareStatement(queryPrefix + " " + translator.translate(query) + " " + querySuffix);

        for (SubQuery subQuery : query.getSubQueries()) {
            if (subQuery.getParserId() == null) {
                continue;
            }

            if (subQuery.getValue() instanceof String) {
                ps.setString(current++, subQuery.getValue().toString());
            } else if (subQuery.getValue() instanceof Integer) {
                ps.setInt(current++, (int) subQuery.getValue());
            } else if (subQuery.getValue() instanceof Date) {
                ps.setDate(current++, (Date) subQuery.getValue());
            } else if (subQuery.getValue() == null) {
                ps.setNull(current++, 0);
            } else {
                ps.setObject(current++, subQuery.getValue());
            }
        }

        return ps;
    }

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public Translator getTranslator() {
        return translator;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
