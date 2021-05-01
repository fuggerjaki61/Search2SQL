package com.search2sql.impl.parser;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TaggedParser extends Parser {

    private final String tag;

    private final char quotation;

    private final String regex;

    public TaggedParser(String tag) {
        this(tag, Character.MIN_VALUE);
    }

    public TaggedParser(String tag, boolean quoted) {
        this(tag, quoted ? '"' : Character.MIN_VALUE);
    }

    public TaggedParser(String tag, char quotation) {
        this.tag = tag;
        this.quotation = quotation;

        this.regex = "^\\s*" + tag + "\\s*:\\s*(?<value>\\S*)\\s*";
    }

    @Override
    public boolean isParserFor(String subQuery) {
        if (subQuery != null && subQuery.matches(regex)) {
            Matcher m = Pattern
                    .compile(regex)
                    .matcher(subQuery);

            if (m.find()) {
                String value = m.group("value");

//                if (quotation == Character.MIN_VALUE && value.contains(qu)) {
//
//                } else {
//
//                }
            }
        }

        return false;
    }

    protected String parseValue(String subQuery) throws InvalidSearchException {
        Matcher m = Pattern
                .compile(regex)
                .matcher(subQuery);

        if (m.find()) {
            return m.group("value");
        } else {
            throw new InvalidSearchException(0);
        }
    }

    public String getTag() {
        return tag;
    }

    public char getQuotation() {
        return quotation;
    }
}
