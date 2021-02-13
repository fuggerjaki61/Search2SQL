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

    @Override
    public boolean isParserFor(String subQuery) {
        if (quotation == Character.MIN_VALUE) {
            return false;
        }

        return subQuery != null && subQuery.matches("^\\s*" + quotation + ".*" + quotation + "\\s*$");
    }
}
