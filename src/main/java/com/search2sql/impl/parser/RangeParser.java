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
 * <i>...152</i> (any values below 152 (or 152) are good) or <i>55..</i> (any values above 55 (or 55) are good). The examples
 * use numbers but for example dates could be also used.
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

    /**
     * This is the final field that defines which delimiter is used.<br>
     * If no value was set, it defaults to <code>\.{2,3}</code>.
     */
    private final String delimiter;

    /**
     * This is the final field that defines if and which quote can be used.<br>
     * If no value was set, it defaults to <code>'\u0000</code> (no quotes allowed).
     */
    private final char quotation;

    /**
     * This is a final field that defines an utility RegEx used in the pre-made method {@link RangeParser#parseValue(String)}.
     * It's generated based on the delimiter RegEx.
     */
    private final String regex;

    /**
     * This constructor sets the delimiter to the default value <code>\.{2,3}</code> (two or three points).
     * <br><br>
     * If you want to define a custom quote, use {@link RangeParser#RangeParser(String)} or {@link RangeParser#RangeParser(String, char)}.
     */
    public RangeParser() {
        this("\\.{2,3}"); // call another constructor and set the default value
    }

    /**
     * This constructor sets the delimiter to the value you choose.
     * <br><br>
     * If you want to define that the parser can use a quote, use {@link RangeParser#RangeParser(String, char)}.
     *
     * @param delimiter range delimiter that will be used (must be valid RegEx)
     */
    public RangeParser(String delimiter) {
        this(delimiter, Character.MIN_VALUE); // call another constructor with the parameter and add a default value
    }

    /**
     * This constructor sets the delimiter and the quote that will be used.
     *
     * @param delimiter range delimiter that will be used (must be valid RegEx)
     * @param quotation character used as quote delimiter
     */
    public RangeParser(String delimiter, char quotation) {
        // set the two values
        this.delimiter = delimiter;
        this.quotation = quotation;

        // generate the regex based on the quotation
        this.regex = "^\\s*" + quotation + "(?<value>.*)" + quotation + "\\s*$";
    }

    /**
     * This method checks if the given SubQuery is valid for parsing with this Parser.
     * <br><br>
     * This implementation checks if the SubQuery matches this scheme: anything + delimiter + anything
     *
     * @param subQuery split part of the whole search query
     *
     * @return if the parser can parse this value
     */
    @Override
    public boolean isParserFor(String subQuery) {
        // check if the delimiter wasn't set
        if (delimiter == null) {
            // without a delimiter this parser is useless and can't parse anything
            return false;
        }

        // check if the SubQuery contains the delimiter
        return subQuery.matches("^.*" + delimiter + ".*$");
    }

    /**
     * This is an utility method used for extracting the values of the range. If a quote is used, the values inside the
     * quote will be retrieved.
     *
     * @param subQuery current SubQuery
     *
     * @return range values (without quote)
     */
    protected String[] parseValue(String subQuery) {
        String[] split = subQuery.split(delimiter); // split the values by the delimiter

        // check if a quotation was used, if no quotation was used just return the split values
        if (quotation != Character.MIN_VALUE) {
            // iterate over all split values
            for (int i = 0; i < split.length; i++) {
                // create a matcher to retrieve the value inside quotes
                Matcher m = Pattern
                        .compile(regex)
                        .matcher(split[i]);

                // check if something was found
                // if nothing was found, no quotation was used and just the split value is returned
                if (m.find()) {
                    split[i] =  m.group("value"); // replace the value without the quote
                }
            }
        }

        // return the results
        return split;
    }

    /**
     * This method returns the delimiter that is used to mark the SubQuery as a range. The delimiter must be a valid
     * RegEx.
     *
     * @return the used delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * This method returns the quotation that can be used for values. The default value is <code>'\u0000'</code> (no
     * quotation supported) but it can be any character.
     *
     * @return the used quotation
     */
    public char getQuotation() {
        return quotation;
    }
}
