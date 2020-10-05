package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;
import com.search2sql.query.SubQuery;

public class QuotedParser extends Parser {

    private final char quotationChar;

    public QuotedParser() {
        quotationChar = '"';
    }

    public QuotedParser(char quotationChar) {
        this.quotationChar = quotationChar;
    }

    @Override
    public boolean isParserFor(String subQuery) {
        return subQuery.matches("[\\s]*" + quotationChar + ".*" + quotationChar + "[\\s]*");
    }

    @Override
    public SubQuery parse(String subQuery) {
        return new SubQuery("quoted", "simple", subQuery.trim().substring(1, subQuery.length() - 1));
    }

    public char getQuotationChar() {
        return quotationChar;
    }
}
