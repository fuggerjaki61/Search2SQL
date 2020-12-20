package com.search2sql;

import com.search2sql.exception.IllegalUseException;
import com.search2sql.exception.i18n.ExceptionHandler;
import com.search2sql.exception.i18n.LocalizedExceptionHandler;
import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.FileTranslator;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import com.search2sql.translator.Translator;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the builder class for {@link Search}.
 * <br><br>
 * This class can be used to construct a Search object. While constructing such objects it uses the same default values
 * like the Search class, so just setting a TableConfig and the calling the {@link SearchBuilder#build()} method generates
 * the same Search like calling {@link Search#Search(TableConfig)}.<br>
 * Also this builder provides two methods to construct a TableConfig object more easily:
 * <ul>
 *     <li>{@link SearchBuilder#setTableName(String)}</li>
 *     <li>{@link SearchBuilder#addColumn(Column)}</li>
 * </ul>
 */
public final class SearchBuilder {

    private String tableName;
    private ArrayList<Column> columns;

    private TableConfig tableConfig;
    private Interpreter interpreter;
    private Translator translator;
    private ExceptionHandler exceptionHandler;

    /**
     * Basic constructor initializing default values.
     */
    public SearchBuilder() {
        // set the default implementations
        interpreter = new BasicInterpreter();
        translator = new FileTranslator();
        exceptionHandler = new LocalizedExceptionHandler();
    }

    /**
     * This method sets the table name of the TableConfig that will be constructed while building the Search.
     *
     * @param tableName name of the table for the TableConfig
     * @return this (builder pattern)
     */
    public SearchBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * This method adds a column to the TableConfig that will be constructed while building the Search.
     *
     * @param column column to add to the TableConfig
     * @return this (builder pattern)
     */
    public SearchBuilder addColumn(Column column) {
        if (columns == null) {
            // initialize the list if it's still null
            columns = new ArrayList<>();
        }

        // add the column to the list
        columns.add(column);

        return this;
    }

    /**
     * This method sets the TableConfig object that will be used later for interpreting and translating the expression.
     *
     * @param tableConfig TableConfig that will be used
     * @return this (builder pattern)
     */
    public SearchBuilder setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
        return this;
    }

    /**
     * This method sets the implementation of the <code>Interpreter</code> that will be used later for interpreting
     * the search expression.
     *
     * @param interpreter implementation that will be used
     * @return this (builder pattern)
     */
    public SearchBuilder setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
        return this;
    }

    /**
     * This method sets the implementation of the <code>Translator</code> that will be used later for translating
     * the search expression.
     *
     * @param translator implementation that will be used
     * @return this (builder pattern)
     */
    public SearchBuilder setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    /**
     * This method sets the implementation of the <code>ExceptionHandler</code> that can be used to get a message for
     * the {@link com.search2sql.exception.InvalidSearchException InvalidSearchException}.
     *
     * @param exceptionHandler implementation that will be used
     * @return this (builder pattern)
     */
    public SearchBuilder setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * This method constructs the {@link Search} object. 
     * <br><br>
     * <b>Important</b><br>
     * You must set a TableConfig before building the search or else an {@link IllegalUseException} is thrown.<br>
     * Use {@link SearchBuilder#setTableConfig(TableConfig)} or {@link SearchBuilder#setTableName(String)} (optional)
     * and {@link SearchBuilder#addColumn(Column)}
     *
     * @return constructed Search object
     */
    public Search build() {
        if (tableName != null && columns != null) {
            // tableName and some columns were set so construct the TableConfig
            tableConfig = new TableConfig(tableName, columns);
        }

        if (tableConfig == null) {
            // TableConfig can't still be null
            throw new IllegalUseException("You must specify a TableConfig with the 'setTableConfig(TableConfig)' method or" +
                    "use 'setTableName(String)' and 'addColumn(Column)'.",
                    new NullPointerException());
        }

        // build the search and return it
        return new Search(tableConfig, interpreter, translator, exceptionHandler);
    }
}
