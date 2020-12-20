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

/**
 * This is the library's one-stop shop for all utilities.
 * <br><br>
 * <b>Note</b><br>
 * This class can be configured with the {@link SearchBuilder}.
 * <br><br>
 * This class is a simple utility that enhances the library's use with the JDBC API. It uses the most recent, best
 * implementation of the {@link Interpreter} class and {@link Translator} class to construct a {@link PreparedStatement}
 * while not limiting the usage of the JDBC API.<br>
 * The class provides a multiple methods to do so:
 * <ul>
 *     <li>
 *         {@link Search#prepareStatement(String, Connection, String, String, int)}<br>
 *         This method is the base of all others that just overload this method. It takes a search expression as the first
 *         parameter and a JDBC {@link Connection} to prepare the statement. It has three additional parameters to add
 *         a prefix and suffix string to the generated sql query and also an index where to start. This index is used when
 *         the query prefix contains parameter values that the user wants to set, e.g. prefix: <code>where some = ? and
 *         &lt;generated sql&gt;</code>; the user wants to replace <code>some = ?</code> with his own value.
 *     </li>
 *     <li>
 *         {@link Search#preparedStatement(String, Connection, String, int)}<br>
 *         This is the same as the previous method. In this case an empty string is taken as suffix, so no suffix is
 *         added.
 *     </li>
 *     <li>
 *         {@link Search#preparedStatement(String, Connection, String)}<br>
 *         This is the same as the previous method, but in this case also the startIndex is replaced by the default value
 *         <code>1</code>. As a consequence no parameter exists in the suffix.
 *     </li>
 * </ul>
 */
public final class Search {

    private final TableConfig tableConfig;
    private final Interpreter interpreter;
    private final Translator translator;
    private final ExceptionHandler exceptionHandler;

    /**
     * This is the default constructor for this class. It sets the {@link TableConfig} it got as a parameter
     * and initializes default implementations of {@link Interpreter}, {@link Translator} and {@link ExceptionHandler}.
     * <br><br>
     * Currently these are the implementations used:
     * <ul>
     *     <li>{@link BasicInterpreter} for the <code>Interpreter</code> class</li>
     *     <li>{@link FileTranslator} for the <code>Translator</code> class</li>
     *     <li>{@link LocalizedExceptionHandler} for the <code>ExceptionHandler</code> class</li>
     * </ul>
     *
     * @param tableConfig TableConfig that will be used
     */
    public Search(TableConfig tableConfig) {
        // sets the tableConfig and initializes the implementations
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

    /**
     * This method overloads {@link Search#prepareStatement(String, Connection, String, String, int)} and just uses an
     * empty string as the default value for the query suffix and a <code>1</code> as the default value for the start index.
     *
     * @param search user search expression
     * @param connection JDBC connection to prepare the statement
     * @param queryPrefix prefix to add in front of the generated query
     * @return fully prepared statement
     * @throws SQLException thrown if the search expression is invalid
     * @throws InvalidSearchException thrown if there is a problem with the JDBC API
     */
    public PreparedStatement preparedStatement(String search, Connection connection,
                                               String queryPrefix) throws SQLException, InvalidSearchException {
        return prepareStatement(search, connection, queryPrefix, "", 1);
    }

    /**
     * This method overloads {@link Search#prepareStatement(String, Connection, String, String, int)} and just uses an
     * empty string as the default value for the query suffix.
     *
     * @param search user search expression
     * @param connection JDBC connection to prepare the statement
     * @param queryPrefix prefix to add in front of the generated query
     * @param startIndex index to start setting the parameters
     * @return fully prepared statement
     * @throws SQLException thrown if the search expression is invalid
     * @throws InvalidSearchException thrown if there is a problem with the JDBC API
     */
    public PreparedStatement preparedStatement(String search, Connection connection,
                                               String queryPrefix, int startIndex) throws SQLException, InvalidSearchException {
        return prepareStatement(search, connection, queryPrefix, "", startIndex);
    }

    /**
     * This is the basic method that implements the logic. All other methods overload this one.
     * <br><br>
     * It takes a search expression as the first parameter and a JDBC {@link Connection} to prepare the statement.
     * It has three additional parameters to add a prefix and suffix string to the generated sql query and also an index
     * where to start. This index is used when the query prefix contains parameter values that the user wants to set,
     * e.g. prefix: <code>where some = ? and &lt;generated sql&gt;</code>; the user wants to replace <code>some = ?</code>
     * with his own value.
     *
     * @param search user search expression
     * @param connection JDBC connection to prepare the statement
     * @param queryPrefix prefix to add in front of the generated query
     * @param querySuffix suffix to add after the generated query
     * @param startIndex index to start setting the parameters
     * @return fully prepared statement
     * @throws InvalidSearchException thrown if the search expression is invalid
     * @throws SQLException thrown if there is a problem with the JDBC API
     */
    public PreparedStatement prepareStatement(String search, Connection connection, String queryPrefix,
                                               String querySuffix, int startIndex) throws InvalidSearchException, SQLException {
        // interpret the search
        Query query = interpreter.interpret(search, tableConfig);

        // initialize the counter variable
        int current = startIndex;
        // prepare the statement with the given prefix, the translated query and the suffix
        PreparedStatement ps = connection.prepareStatement(queryPrefix + " " + translator.translate(query) + " " + querySuffix);

        // iterate over every SubQuery to set the parameters
        for (SubQuery subQuery : query.getSubQueries()) {
            // check if parser id is null
            if (subQuery.getParserId() == null) {
                /*
                 * parser id is null so this SubQuery must be a special one like a logical operator that is clearly no
                 * parameter. ignore this SubQuery and continue with the next one
                 */
                continue;
            }

            // checks for the type of the values and uses the matching method
            if (subQuery.getValue() instanceof String) {
                // the value is a string so set it and increment the current index
                ps.setString(current++, subQuery.getValue().toString());
            } else if (subQuery.getValue() instanceof Integer) {
                // the value is an Integer or simple int
                ps.setInt(current++, (int) subQuery.getValue());
            } else if (subQuery.getValue() instanceof Date) {
                // the value is a java.sql.Date
                ps.setDate(current++, (Date) subQuery.getValue());
            } else if (subQuery.getValue() == null) {
                // the value is null; so set null at the current position
                ps.setNull(current++, 0);
            } else {
                // the value isn't supported, so use the generic value
                ps.setObject(current++, subQuery.getValue());
            }
        }

        // return the fully prepared statement
        return ps;
    }

    /**
     * This method returns the TableConfig that was specified while initializing this search.
     *
     * @return used TableConfig
     */
    public TableConfig getTableConfig() {
        return tableConfig;
    }

    /**
     * This method returns the implementation of the <code>Interpreter</code> that was specified while initializing this
     * search.
     *
     * @return used implementation of <code>Interpreter</code>
     */
    public Interpreter getInterpreter() {
        return interpreter;
    }

    /**
     * This method returns the implementation of the <code>Translator</code> that was specified while initializing this
     * search.
     *
     * @return used implementation of <code>Translator</code>
     */
    public Translator getTranslator() {
        return translator;
    }

    /**
     * This method returns the implementation of the <code>ExceptionHandler</code> that was specified while initializing
     * this search.
     *
     * @return used implementation of <code>ExceptionHandler</code>
     */
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
