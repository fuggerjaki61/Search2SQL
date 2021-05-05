package com.search2sql.impl.parser.provided;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.impl.parser.RangeParser;
import com.search2sql.parser.SearchParser;
import com.search2sql.query.SubQuery;

import java.text.DateFormat;
import java.util.Locale;

@SearchParser("default.date")
public class DateParser extends RangeParser {

    private DateFormat dateFormat;

    public DateParser() {
        this(Locale.getDefault());
    }

    public DateParser(Locale locale) {
        super();

        this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    }

    public DateParser(Locale locale, String delimiter) {
        super(delimiter);

        this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    }

    @Override
    public boolean isParserFor(String subQuery) {


        return super.isParserFor(subQuery);
    }

    @Override
    public SubQuery parse(String subQuery) throws InvalidSearchException {
        //new SimpleDateFormat()

        return null;
    }
}
