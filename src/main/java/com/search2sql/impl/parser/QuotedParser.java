package com.search2sql.impl.parser;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This an abstract parser that can be implemented to inherit the ability to parse quoted values like <i>"text in quotes"</i>.
 * <br><br>
 * <b>Important</b><br>
 * The abilities this parser has are based on the interpreter used. The deprecated {@link com.search2sql.impl.interpreter.BasicInterpreter BasicInterpreter}
 * doesn't support this. Use the {@link com.search2sql.impl.interpreter.LogicInterpreter LogicInterpreter} instead or else this
 * class is useless.
 * <br><br>
 * This parser parses anything that is enclosed in the specified char. It can be everything from <code>*</code> (asterisks),
 * <code>'</code> (single quotes) to any letter or whitespace (not recommended).<br>
 * The default quote used when no other one is specified in the constructor is: <code>"</code> (double quotes).<br>
 * The <code>QuotedParser</code> supports defining custom quotes instead of the default one by setting it in the constructor
 * {@link QuotedParser#QuotedParser(char)}.
 * <br><br>
 * When inheriting this abstract parser only the {@link QuotedParser#parse(String)} method must be implemented. The
 * {@link QuotedParser#isParserFor(String)} method is already implemented, it matches everything enclosed in given quote.
 * It may be overridden when you want to perform extra checks before parsing.<br>
 * When implementing the {@link QuotedParser#parse(String)} method {@link QuotedParser#parseValue(String)} may help you.
 * It retrieves the value inside the quotes and returns it for you to do more parsing.
 *
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public abstract class QuotedParser extends Parser {

    /**
     * This final char field contains the value of the quote that is used for parsing. It is set by the constructor and can't
     * be changed after that.
     * <br><br>
     * If no custom value was set, it defaults to <code>"</code> (double quotes).
     */
    private final char quotation;

    /**
     * This final string field contains the generated regex that is used to match the sub query. This regex is based on
     * the quotation character used.
     */
    private final String regex;

    /**
     * This is a default constructor that performs no action.
     * <br><br>
     * This constructor sets the quote to the default value <code>"</code> (double quotes).
     * <br><br>
     * If you want to define a custom quote, use {@link QuotedParser#QuotedParser(char)}.
     */
    public QuotedParser() {
        // let the other constructor handle the value setting
        this('"');
    }

    /**
     * This is a default constructor that performs no action.
     * <br><br>
     * This constructor sets the quote based on the value you give it.
     *
     * @param quotation the quote that is going to be used
     */
    public QuotedParser(char quotation) {
        // set the quote
        this.quotation = quotation;

        // build the regex based on the quote
        this.regex = "^\\s*" + quotation + "(?<value>.*)" + quotation + "\\s*$";
    }

    /**
     * Returns the char value of the quote that is used for parsing.
     *
     * @return the quote
     */
    public char getQuotation() {
        // return the value
        return quotation;
    }

    /**
     * This method checks if the given SubQuery is valid for parsing with this Parser.
     * <br><br>
     * This implementation checks if the SubQuery is not null and if it matches the format of a quote.
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating whether this can be parsed by this Parser or not
     */
    @Override
    public boolean isParserFor(String subQuery) {
        // must not be null and must match the generated regex
        return subQuery != null && subQuery.matches(regex);
    }

    /**
     * This method is an utility that can be used by any implementation of this class. The method takes the string and
     * retrieves the value inside the quotes. It can be used when only the value inside the quotes matter.
     * <br><br>
     * Whitespaces inside the quote are <i>NOT</i> cut and every character inside the quote is returned.
     *
     * @param subQuery the given SubQuery
     * @return value inside the quote
     * @throws InvalidSearchException this exception should never be thrown
     */
    protected String parseValue(String subQuery) throws InvalidSearchException {
        // compile the regex and generate a Matcher object for the given sub query
        Matcher m = Pattern
                .compile(regex)
                .matcher(subQuery);

        // check if the regex matches (should be always the case, because it was already check by the isParserFor() method).
        if (m.find()) {
            // return the regex group named value
            return m.group("value");
        } else {
            // this can only happen when there's wrong user input or a wrong implementation of the interpreter
            throw new InvalidSearchException(0); // the problem is unknown/not specified
        }
    }
}
