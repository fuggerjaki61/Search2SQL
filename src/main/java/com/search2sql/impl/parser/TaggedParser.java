package com.search2sql.impl.parser;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is an abstract parser that can parse tags like <code>tag: value</code>.
 * <br><br>
 * <b>Important</b><br>
 * The abilities this parser has are based on the interpreter used. The deprecated {@link com.search2sql.impl.interpreter.BasicInterpreter BasicInterpreter}
 * doesn't support this. Use the {@link com.search2sql.impl.interpreter.LogicInterpreter LogicInterpreter} instead or else this
 * class is useless.
 * <br><br>
 * If multiple parsers could parse the same type but only one should do it, the TaggedParser can be used. It checks if
 * a value as a fix prefix (tag). If this prefix exists the value is parsed by this parser.
 * <br><br>
 * When inheriting this abstract parser only the {@link TaggedParser#parse(String)} method must be implemented. The
 * {@link TaggedParser#isParserFor(String)} method is already implemented, it matches if the given tag exists.
 * It may be overridden when you want to perform extra checks before parsing.<br>
 * When implementing the {@link TaggedParser#parse(String)} method {@link TaggedParser#parseValue(String)} may help you.
 * It retrieves the tagged value (inside the quotes) and returns it for you to do more parsing.
 *
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public abstract class TaggedParser extends Parser {

    private final String tag;

    private final char quotation;

    private final String regex;

    /**
     * This constructor sets the mandatory tag value. You must set a tag value.
     *
     * @param tag prefix for this SubQuery
     */
    public TaggedParser(String tag) {
        this(tag, Character.MIN_VALUE); // call another constructor with the parameter and a default value
    }

    /**
     * This constructor is similar to {@link TaggedParser#TaggedParser(String, char)} but it gives the ability to
     * set a boolean as quote.
     * <br><br>
     * If the <code>quoted</code> parameter is <code>true</code>, the default quote <code>"</code> (double quotes) is used. If the value
     * is <code>false</code> no quote will be used.
     *
     * @param tag prefix for this SubQuery
     * @param quoted boolean if this parser can use quotes
     */
    public TaggedParser(String tag, boolean quoted) {
        // set the tag and set the two possible, default values
        this(tag, quoted ? '"' : Character.MIN_VALUE);
    }

    /**
     * This constructor sets the tag and the quotation.
     * <br><br>
     * The quotation can be any character. The value <code>'\u0000'</code> means no quote used.
     *
     * @param tag prefix for this SubQuery
     * @param quotation the quote that is going to be used
     */
    public TaggedParser(String tag, char quotation) {
        // set the two values
        this.tag = tag;
        this.quotation = quotation;

        // generate a RegEx based on the tag value
        this.regex = "^\\s*" + tag + "\\s*:\\s*(?<value>\\S*)\\s*";
    }

    /**
     * This method checks if the given SubQuery is valid for parsing with this Parser.
     * <br><br>
     * This implementation checks if the SubQuery has the prefix (tag),
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating whether this can be parsed by this Parser or not
     */
    @Override
    public boolean isParserFor(String subQuery) {
        // subQuery can't be null and it must match the RegEx
        return subQuery != null && subQuery.matches(regex);
    }

    /**
     * This method is an utility that can be used by any implementation of this class. The method takes the string and
     * retrieves the tag value (inside the quotes).
     *
     * @param subQuery the given SubQuery
     * @return tag value (inside the quote)
     * @throws InvalidSearchException this exception should never be thrown
     */
    protected String parseValue(String subQuery) throws InvalidSearchException {
        // generate a matcher to find the tag value
        Matcher m = Pattern
                .compile(regex)
                .matcher(subQuery);

        // check if a tag value was found
        if (m.find()) {
            // retrieve the found value
            String value = m.group("value");

            // check if quotations are allowed
            if (quotation != Character.MIN_VALUE) {
                // generate a new matcher to find the value in the tag
                m = Pattern
                        .compile("^\\s*" + quotation + "(?<value>.*)" + quotation + "\\s*$")
                        .matcher(value);

                // check if a value inside a quote was found
                // if no value was found, the normal value is returned
                if (m.find()) {
                    // a quote was found, replace the value without the quote
                    value = m.group("value");
                }
            }

            // return the tag value (without quote)
            return value;
        } else {
            // no tag value was found, throw an exception then
            throw new InvalidSearchException(0);
        }
    }

    /**
     * Get the used tag (prefix) value.
     *
     * @return used tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Returns the char value of the quote that is used for parsing.
     * <br><br>
     * <code>'\u0000'</code> means no quote is used.
     *
     * @return used quote
     */
    public char getQuotation() {
        return quotation;
    }
}
