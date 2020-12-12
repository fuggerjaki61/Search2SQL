package com.search2sql;

import com.search2sql.exception.IllegalUseException;
import com.search2sql.exception.i18n.ExceptionHandler;
import com.search2sql.exception.i18n.LocalizedExceptionHandler;
import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.FileTranslator;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.table.TableConfig;
import com.search2sql.translator.Translator;

public final class SearchBuilder {

    private TableConfig tableConfig;
    private Interpreter interpreter;
    private Translator translator;
    private ExceptionHandler exceptionHandler;

    public SearchBuilder() {
        interpreter = new BasicInterpreter();
        translator = new FileTranslator();
        exceptionHandler = new LocalizedExceptionHandler();
    }

    public SearchBuilder setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
        return this;
    }

    public SearchBuilder setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
        return this;
    }

    public SearchBuilder setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public SearchBuilder setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public Search build() {
        if (tableConfig == null) {
            throw new IllegalUseException("You must specify a TableConfig with the 'setTableConfig(TableConfig)' method.",
                    new NullPointerException());
        }

        return new Search(tableConfig, interpreter, translator, exceptionHandler);
    }
}
