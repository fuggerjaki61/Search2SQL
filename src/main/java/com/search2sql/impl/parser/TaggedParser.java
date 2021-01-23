package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;

public abstract class TaggedParser extends Parser {

    private final String tag;

    private final boolean canHaveQuote;

    public TaggedParser(String tag) {
        this(tag, false);
    }

    public TaggedParser(String tag, boolean canHaveQuote) {
        this.tag = tag;
        this.canHaveQuote = canHaveQuote;
    }

    public String getTag() {
        return tag;
    }

    public boolean canHaveQuote() {
        return canHaveQuote;
    }
}
