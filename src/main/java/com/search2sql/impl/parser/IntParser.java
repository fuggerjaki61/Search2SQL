package com.search2sql.impl.parser;

import com.search2sql.query.SubQuery;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;

/**
 * This implementation of {@link Parser} is for parsing integers.
 */
@SearchParser("default.int")
public class IntParser extends Parser {

    /**
     * This method is a simple implementation just to check if the string can be parsed as a number.
     * Depending on the settings these numbers are allowed:<br>
     * <code>42</code> (interpreted as positive by default)<br>
     * <code>-42</code> (depending if <code>negative</code> is set to true)<br>
     * <code>+42</code>
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating if this sub-query is parsable
     */
    @Override
    public boolean isParserFor(String subQuery) {
        return subQuery.matches("(\\+|-|)[\\d]+");
    }

    /**
     * This method is a simple implementation to parse the sub-query to a {@link SubQuery}.
     *
     * @param subQuery split part of the whole search query
     * @return parsed value in form of a SubQuery
     */
    @Override
    public SubQuery parse(String subQuery) {
        // just sets parserId, type and parses string to int
        return new SubQuery("int", "simple", Integer.parseInt(subQuery));
    }
}
