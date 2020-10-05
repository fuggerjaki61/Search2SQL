package com.search2sql.impl.parser;

import com.search2sql.query.SubQuery;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * This is a default implementation of the {@link Parser}. This parser is for parsing dates with the german format.
 * <br /><br />
 * This implementation has no settings.
 * <br /><br />
 * This implementation parses every date with the german format <code>dd.mm.yyyy</code>.
 */
@SearchParser("date")
public class DateParser extends Parser {

    /**
     * This method checks if this sub-query is parsable by this parser.<br />
     * This method will every match every date with the german format.s
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating if the sub-query is parsable
     */
    @Override
    public boolean isParserFor(String subQuery) {
        // checks if sub-query mathches date pattern
        return subQuery.matches("\\d{2}\\.\\d{2}\\.\\d{4}");
    }

    /**
     * This method parses the sub-query to the more complex form a {@link SubQuery}.
     *
     * @param subQuery split part of the whole search query
     * @return parsed value in form of a SubQuery
     */
    @Override
    public SubQuery parse(String subQuery) {
        // parses date and inserts it into a SubQuery
        return new SubQuery("date", "simple", LocalDate.parse(subQuery, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
