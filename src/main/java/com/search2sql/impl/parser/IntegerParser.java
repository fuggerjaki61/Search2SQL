package com.search2sql.impl.parser;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.SearchParser;
import com.search2sql.query.SubQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SearchParser("default.int")
public class IntegerParser extends RangeParser {

    private static final String normalRegex = "^\\s*(?:|\\+|-)\\d+\\s*$";

    private final String rangeRegex;

    public IntegerParser() {
        this(true);
    }

    public IntegerParser(boolean range) {
        this(range ? "\\.{2,3}" : null);
    }

    public IntegerParser(String delimiter) {
        super(delimiter);

        rangeRegex = "^\\s*(?:|\\+|-)\\d*\\s*" + delimiter + "\\s*(?:|\\+|-)\\d*\\s*$";
    }

    @Override
    public boolean isParserFor(String subQuery) {
        if (getDelimiter() != null) {
            return subQuery.matches(rangeRegex) || subQuery.matches(normalRegex);
        }

        return subQuery.matches(normalRegex);
    }

    @Override
    public SubQuery parse(String subQuery) throws InvalidSearchException {
        if (getDelimiter() != null && subQuery.matches(rangeRegex)) {
            Matcher matcher = Pattern.compile("^\\s*(?<first>(?:|\\+|-)\\d*)\\s*" + getDelimiter() + "\\s*(?<last>(?:|\\+|-)\\d*)\\s*$").matcher(subQuery);

            if (matcher.find()) {
                String first = matcher.group("first");
                String last = matcher.group("last");

                if (!first.isEmpty() && last.isEmpty()) {
                    int firstParsed;

                    try {
                        firstParsed = Integer.parseInt(first);
                    } catch (NumberFormatException nfe) {
                        throw new InvalidSearchException(3);
                    }

                    return new SubQuery("default.int", "range.min", firstParsed);
                } else if (first.isEmpty() && !last.isEmpty()) {
                    int lastParsed;

                    try {
                        lastParsed = Integer.parseInt(last);
                    } catch (NumberFormatException nfe) {
                        throw new InvalidSearchException(3);
                    }

                    return new SubQuery("default.int", "range.max", lastParsed);
                } else if (!first.isEmpty()) {
                    int firstParsed;
                    int lastParsed;

                    try {
                        firstParsed = Integer.parseInt(first);
                        lastParsed = Integer.parseInt(last);
                    } catch (NumberFormatException nfe) {
                        throw new InvalidSearchException(3);
                    }

                    if (firstParsed > lastParsed) {
                        throw new InvalidSearchException(4);
                    }

                    return new SubQuery("default.int", "range", new int[]{firstParsed, lastParsed});
                } else {
                    throw new InvalidSearchException(5);
                }
            } else {
                throw new InvalidSearchException(0);
            }
        } else if (subQuery.matches(normalRegex)) {
            int parsed;

            try {
                parsed = Integer.parseInt(subQuery);
            } catch (NumberFormatException nfe) {
                throw new InvalidSearchException(3);
            }

            return new SubQuery("default.int", "simple", parsed);
        } else {
            throw new InvalidSearchException(0);
        }
    }
}
