package com.search2sql.impl.parser.provided;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.impl.parser.RangeParser;
import com.search2sql.parser.SearchParser;
import com.search2sql.query.SubQuery;

@SearchParser("default.date")
public class DateParser extends RangeParser {

    @Override
    public SubQuery parse(String subQuery) throws InvalidSearchException {
        return null;
    }
}
