package com.search2sql.interpreter;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import com.search2sql.query.Query;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;

/**
 * The <code>Interpreter</code> is the tool used in the second phase <code>Interpreting</code>.
 * The second phase is very close to the first phase <code>Parsing</code>.
 * <br><br>
 * The <code>Interpreter</code>'s purpose is to interpret the simple string form in the more complex version of
 * a {@link Query} and its {@link com.search2sql.query.SubQuery SubQueries} with the help of
 * {@link Parser Parsers}. The interpret just has one method {@link Interpreter#interpret(String, TableConfig)}.
 * An implementation of this method could look like this:
 * <ol>
 *  <li>
 *      Find all available <code>Parsers</code> by the id specified by the {@link SearchParser @SearchParser}
 *      annotation.
 *  </li>
 *  <li>
 *      Split the search query in sub-queries.
 *  </li>
 *  <li>
 *      Pass sub-queries to the right <code>Parser</code> based on the {@link TableConfig} and the specified
 *      {@link Column Columns}.
 *  </li>
 *  <li>
 *      Merge the parsed sub-queries in one complete one.
 *  </li>
 * </ol>
 * <br>
 * <b>Note</b><br>
 * Implementing a custom <code>Interpreter</code> is also described in the overall documentation. You can also take a
 * look at the provided <code>Interpreter</code>'s.
 * <br><br>
 * <b>See Also</b><br>
 * <i>Overall Documentation</i><br>
 * {@link Parser com.searchflow.parser.Parser}<br>
 * {@link SearchParser com.searchflow.parser.SearchParser}<br>
 * {@link com.search2sql.query.Query com.searchflow.query.Query}<br>
 * {@link com.search2sql.query.SubQuery com.searchflow.query.SubQuery}
 * {@link com.search2sql.table.TableConfig com.searchflow.table.TableConfig}
 * {@link com.search2sql.table.Column com.searchflow.table.Column}
 * <br><br>
 * <b>Known Implementations</b><br>
 * {@link com.search2sql.impl.interpreter.BasicInterpreter}<br>
 * {@link com.search2sql.impl.interpreter.LogicInterpreter}
 */
public abstract class Interpreter {

    /**
     * This method is the main logic behind the <code>Parsing</code> phase.
     * <br><br>
     * This method is for interpreting the search query with the given information about the table layout.
     * This method is performed for each query again and therefore should have a good performance. Looking up the
     * parsers should be done via the Reflections API. To improve performance that should be done <i>ONCE</i>
     * for all parsers or should be done <i>ONCE</i> for each parser.
     * An implementation can be seen in {@link com.search2sql.impl.interpreter.BasicInterpreter} or
     * {@link com.search2sql.impl.interpreter.LogicInterpreter}.
     * <br><br>
     * <b>Note</b><br>
     * If you don't have to, you should use the given implementations.
     * <br><br>
     * For more information see this class's documentation or overall documentation.
     *
     * @param searchQuery simple string form of the search query
     * @param tableConfig meta-information about the table (column types, etc.)
     * @return parsed & interpreted form of the search query
     */
    public abstract Query interpret(String searchQuery, TableConfig tableConfig) throws InvalidSearchException;

    /**
     * This method just provides a shortcut for the usage of a single column interpretation.
     * This method should be used when performing searches on a single column.
     * <br><br>
     * <b>Note</b><br>
     * This method does <i>NOT</i> perform any logic and thus mustn't be overridden.
     *
     * @param searchQuery simple string from of the search query
     * @param column meta-information about the column type
     * @return parsed & interpreted form of the search query
     */
    public Query interpret(String searchQuery, Column column) throws InvalidSearchException {
        return interpret(searchQuery, new TableConfig(column));
    }
}
