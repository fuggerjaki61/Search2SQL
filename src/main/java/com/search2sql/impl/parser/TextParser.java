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
 */
@SearchParser("text")
public class TextParser extends QuotedParser {

    private boolean quoted = true;

    public TextParser() {
    }

    public TextParser(Boolean quoted) {
        this.quoted = quoted;
    }

    /**
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating if the sub-query is parsable
     */
    @Override
    public boolean isParserFor(String subQuery) {
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
        if (super.isParserFor(subQuery) && quoted) {
            SubQuery query = super.parse(subQuery);

            query.setParserId("text");
            query.setType("quote");

            return query;
        }

        // just wraps parameter
        return new SubQuery("text", "simple", subQuery);
    }
}
