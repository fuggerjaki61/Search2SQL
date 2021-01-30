package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;

public abstract class RangeParser extends Parser {

    private final String delimiter;

    private final char quotation;

    public RangeParser() {
        this("[\\.]{2,3}");
    }

    public RangeParser(String delimiter) {
        this(delimiter, Character.MIN_VALUE);
    }

    public RangeParser(String delimiter, char quotation) {
        this.delimiter = delimiter;
        this.quotation = quotation;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public char getQuotation() {
        return quotation;
    }
}
