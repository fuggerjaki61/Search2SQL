package com.search2sql.impl.parser;

import com.search2sql.parser.Parser;
import com.search2sql.query.SubQuery;

/**
 * This is a special implementation of the {@link Parser}. This parser can be used for parsing quotes.
 * <br /><br />
 * This parser can't be used because it doesn't define the {@link com.search2sql.parser.SearchParser @SearchParser} annotation.
 * This parser is meant to be extended by other parser classes that want to inherit the functionality of parsing quotes.
 * An example for this would be the {@link TextParser}. The functionality must be implemented by the used
 * {@link com.search2sql.interpreter.Interpreter Interpreter} (e.g. {@link com.search2sql.impl.interpreter.BasicInterpreter BasicInterpreter}).
 * If it isn't implemented, this parser has no functionality.<br />
 * The char that begins/ends the quote is set to <code>"</code> (double quotes) as default but can be changed by using the
 * {@link QuotedParser#QuotedParser(char)} constructor.
 *
 * @author fuggerjaki61
 * @since 1.0-echo
 */
public class QuotedParser extends Parser {

    private final char quotationChar;

    /**
     * This is a basic constructor initializing values.
     * <br /><br />
     * The char that begins/ends the quote is set by this constructor to the default value <code>"</code> (double quotes).
     */
    public QuotedParser() {
        // set the quotationChar to "
        quotationChar = '"';
    }

    /**
     * This is a basic constructor initializing values.
     * <br /><br />
     * The char that begins/ends the quote is set by this constructor to the parameter value.
     */
    public QuotedParser(char quotationChar) {
        // just set it
        this.quotationChar = quotationChar;
    }

    /**
     * This method should be used in the <code>isParserFor()</code> method of any subclass.<br />
     * Example:<br /><br />
     * <code>return anotherCondition || super.isParserFor(subQuery);</code>
     *
     * @param subQuery split part of the whole search query
     * @return boolean if the parser can be used for the sub-query
     */
    @Override
    public boolean isParserFor(String subQuery) {
        return subQuery.matches("[\\s]*" + quotationChar + ".*" + quotationChar + "[\\s]*");
    }

    /**
     * This method should be used in the <code>parse()</code> method of any subclass.<br />
     *
     * @param subQuery split part of the whole search query
     * @return parsed SubQuery
     */
    @Override
    public SubQuery parse(String subQuery) {
        return new SubQuery("quoted", "simple", subQuery.trim().substring(1, subQuery.length() - 1));
    }

    /**
     * This method returns the final quotation char that is used to indicated the start/end of the quote.
     *
     * @return quotation char
     */
    public char getQuotationChar() {
        return quotationChar;
    }
}
