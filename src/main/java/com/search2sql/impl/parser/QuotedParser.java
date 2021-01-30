package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;

public abstract class QuotedParser extends Parser {

    private final char quotation;

    public QuotedParser() {
        quotation = '"';
    }

    public QuotedParser(char quotation) {
        this.quotation = quotation;
    }

    public char getQuotation() {
        return quotation;
    }
}
