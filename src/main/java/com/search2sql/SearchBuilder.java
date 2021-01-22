package com.search2sql;

import com.search2sql.exception.IllegalUseException;
import com.search2sql.exception.i18n.ExceptionHandler;
import com.search2sql.exception.i18n.LocalizedExceptionHandler;
import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.FileTranslator;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.table.TableConfig;
import com.search2sql.translator.Translator;

/**
 * This is the builder class for {@link Search}.
 * <br><br>
 * This class can be used to construct a Search object. While constructing such objects it uses the same default values
 * like the Search class, so just setting a TableConfig and the calling the {@link SearchBuilder#build()} method generates
 * the same Search like calling {@link Search#Search(TableConfig)}.
 */
public final class SearchBuilder {

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
     *
     * @return constructed Search object
     */
    public Search build() {
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
