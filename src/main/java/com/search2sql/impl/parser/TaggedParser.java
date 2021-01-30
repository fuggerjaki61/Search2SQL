package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;

public abstract class TaggedParser extends Parser {

    private final String tag;

    private final char quotation;

    public TaggedParser(String tag) {
        this(tag, Character.MIN_VALUE);
    }

    public TaggedParser(String tag, char quotation) {
        this.tag = tag;
        this.quotation = quotation;
    }

    public String getTag() {
        return tag;
    }

    public char getQuotation() {
        return quotation;
    }
}
