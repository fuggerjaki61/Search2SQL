package com.search2sql.translator;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.Parser;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.query.Query;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;

/**
 * This is the only, basic part of the <code>Translating</code> phase.
 * <br>
 * <i>Note:</i> The <code>Translating</code> phase translates the parsed {@link Query} into usable sql.
 * <br>
 * This class is meant to be extended by the custom implementations. Every Translator must implement following
 * method:
 * <br><br>
 * {@link Translator#translate(Query)}
 * <br><br>
 * The <code>translate()</code> method is targeted for the use of the {@link java.sql.PreparedStatement PreparedStatement}.
 * It is recommended to use <code>translate()</code> with the <code>PreparedStatement</code> for safety issues due to the
 * resistance against SQL-Injection.
 * <br><br>
 * An explanation of the purpose and some ideas how to implement a custom one can be also found in each methods' documentation.
 * <br><br>
 * <b>See Also</b><br>
 * {@link Query com.searchflow.query.Query}<br>
 * {@link com.search2sql.query.SubQuery com.searchflow.query.SubQuery}<br>
 * {@link java.sql.PreparedStatement java.sql.PreparedStatement}<br>
 * {@link com.search2sql.interpreter.Interpreter com.searchflow.interpreter.Interpreter}<br>
 * {@link Parser com.searchflow.parser.Parser}
 * <br><br>
 * <b>Known Implementations</b><br>
 * {@link com.search2sql.impl.translator.FileTranslator com.searchflow.impl.translator.FileTranslator}<br>
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
public abstract class Translator {

    /**
     * This method translates the given {@link Query} in a valid part of an sql query that can be used to filter/search
     * the output of that query.
     * <br><br>
     * This method is targeted for the <i>safe</i> use with the {@link java.sql.PreparedStatement PreparedStatement}.
     * This method takes the parsed and interpreted {@link Query} with its list of split {@link com.search2sql.query.SubQuery SubQueries}
     * and then translates it to sql. The output can be different from implementation to implementation. Some implementations just
     * return the search part of the query and other implementations return the whole usable query.<br>
     * But all have one thing in common: the value is not inserted and replaced by <code>?</code> (question mark) to prevent
     * the possibilities of an SQL-Injection attack.<br>
     * A possible output could be:<br>
     * <code>WHERE table.column1 = ? AND table.column2 &lt;= ?</code>
     *
     * @param query parsed and interpreted version of the basic string expression
     * @return string containing usable sql (different for every implementation)
     */
    public abstract String translate(Query query);

    /**
     * This method is just an extension of the {@link Translator#translate(Query)} method. There is no need to override
     * this method since it is already implemented. This method is only used to make a shortcut so the <code>Translator</code>
     * can be used easily.<br>
     * This method just calls the {@link Interpreter#interpret(String, TableConfig)} method to generate the {@link Query}
     * and then forward it to the actual <code>translate()</code> method. Then the result is getting forwarded back to you.<br>
     * The <code>Interpreter</code> that will be received as a parameter will be used to interpret the expression.
     * <br><br>
     * For more information see the documentation of the {@link Translator#translate(Query)} method and of the {@link Interpreter}.
     *
     * @param expression search expression that was entered by the user
     * @param tableConfig configuration of the table
     * @param interpreter interpreter used to interpret the expression
     * @return string containing usable sql (different for every implementation)
     * @throws InvalidSearchException thrown if a problem occurred while parsing
     */
    public String translate(String expression, TableConfig tableConfig, Interpreter interpreter) throws InvalidSearchException {
        // first: interpret the expression with the given interpreter
        // second: translate it with the actual translation method
        // third: pass the result back
        return translate(interpreter.interpret(expression, tableConfig));
    }
}
