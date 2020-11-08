package com.search2sql.impl.parser;

import com.search2sql.query.SubQuery;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;

/**
 * This is a default implementation of the {@link Parser}. This parser is for parsing any strings.
 * <br /><br />
 * This implementation has no settings.
 * <br /><br />
 * This implementation parses literally everything that is not a whitespace.
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
@SearchParser("text")
public class TextParser extends QuotedParser {

    private boolean quoted = true;

    /**
     * Basic constructor performing no action.
     */
    public TextParser() {
        // NOOP
    }

    /**
     * Basic constructor initializing values.
     *
     * @param quoted boolean indicating whether quotes are regarded or not
     */
    public TextParser(Boolean quoted) {
        this.quoted = quoted;
    }

    /**
     * This method checks if the sub-query is not empty or if the sub-query is a quote.
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating if the sub-query is parsable
     */
    @Override
    public boolean isParserFor(String subQuery) {
        // parse everything or check if QuotedParser can parse it
        return subQuery.matches("[\\S]+") || super.isParserFor(subQuery);
    }

    /**
     * This method parses the sub-query to the more complex form a {@link SubQuery}.
     * This method will just wrap the parameter in a <code>SubQuery</code>.
     *
     * @param subQuery split part of the whole search query
     * @return parsed value in form of a SubQuery
     */
    @Override
    public SubQuery parse(String subQuery) {
        // checks if QuotedParser can parse it and if quotes are regarded
        if (super.isParserFor(subQuery) && quoted) {
            // parse it
            SubQuery query = super.parse(subQuery);

            // add metadata
            query.setParserId("text");
            query.setType("quote");

            // return the query
            return query;
        }

        // just wraps parameter
        return new SubQuery("text", "simple", subQuery);
    }
}
