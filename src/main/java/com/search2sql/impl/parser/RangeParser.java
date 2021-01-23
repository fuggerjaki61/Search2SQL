package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;

public abstract class RangeParser extends Parser {

    private final String delimiter;

    private final boolean canHaveQuote;

    public RangeParser() {
        this("[.]{2,3}", false);
    }

    public RangeParser(String delimiter, boolean canHaveQuote) {
        this.delimiter = delimiter;
        this.canHaveQuote = canHaveQuote;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public boolean canHaveQuote() {
        return canHaveQuote;
    }
}
