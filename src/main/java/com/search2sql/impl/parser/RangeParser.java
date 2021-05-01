package com.search2sql.impl.parser;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is an abstract parser that can be extended to inherit the functionality to parse ranges like <i>123...456</i>.
 * <br><br>
 * <b>Important</b><br>
 * The functionality this parser promises is useless when not used with an {@link com.search2sql.interpreter.Interpreter Interpreter}
 * that supports this feature. Currently this is only the {@link com.search2sql.impl.interpreter.LogicInterpreter LogicInterpreter}.
 * <br><br>
 * This parser parses anything that is a range. Ranges need a delimiter to be identified as a range. The default delimiters
 * are two/three points <code>..(.)</code>. Ranges are open-ended to signal that any value above or below are possible
 * <i>...152</i> (any values below 152 (or 152) are good) or <i>55..</i> (any values above 55 (or 55) are good).
 * <br><br>
 * The delimiter is found via RegEx. So a custom delimiter must be a valid RegEx. A delimiter shouldn't be a common
 * character like <i>-</i> (minus) when other values are also parsed because values can be identified as ranges accidentally.
 * <br><br>
 * When implementing this parser only one method must be overridden {@link RangeParser#parse(String)}. The {@link RangeParser#isParserFor(String)}
 * is already implemented.
 *
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public abstract class RangeParser extends Parser {

    private final String delimiter;

    private final char quotation;

    private final String regex;

    public RangeParser() {
        this("\\.{2,3}");
    }

    public RangeParser(String delimiter) {
        this(delimiter, Character.MIN_VALUE);
    }

    public RangeParser(String delimiter, char quotation) {
        this.delimiter = delimiter;
        this.quotation = quotation;

        this.regex = "^\\s*" + quotation + "(?<value>.*)" + quotation + "\\s*$";
    }

    @Override
    public boolean isParserFor(String subQuery) {
        if (delimiter == null) {
            return false;
        }

        return subQuery.matches("^.*" + delimiter + ".*$");
    }

    protected String[] parseValue(String subQuery) throws InvalidSearchException {
        String[] split = subQuery.split(delimiter);

        if (quotation != Character.MIN_VALUE) {
            for (int i = 0; i < split.length; i++) {
                Matcher m = Pattern
                        .compile(regex)
                        .matcher(split[i]);

                if (m.find()) {
                    split[i] =  m.group("value");
                }
            }
        }

        return split;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public char getQuotation() {
        return quotation;
    }
}
