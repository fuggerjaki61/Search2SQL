package com.search2sql.impl.parser;

import com.search2sql.query.SubQuery;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;

import java.util.Set;

/**
 * This is a default implementation of the {@link Parser}. This parser is for parsing any strings.
 * <br><br>
 * This implementation has no settings.
 * <br><br>
 * This implementation parses literally everything that is not a whitespace.
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
@SearchParser("default.text")
public class TextParser extends QuotedParser {

    public TextParser() {
        this(true);
    }

    public TextParser(boolean quoted) {
        super(quoted ? '"' : Character.MIN_VALUE);
    }

    public TextParser(char quotation) {
        super(quotation);
    }

    public boolean isParserFor(String subQuery) {
        if (getQuotation() != Character.MIN_VALUE) {
            return (subQuery.startsWith(String.valueOf(getQuotation())) && subQuery.endsWith(String.valueOf(getQuotation()))) || subQuery.matches("^\\S+$");
        } else {
            return subQuery.matches("^\\S$");
        }
    }

    @Override
    public SubQuery parse(String subQuery) {
        System.out.println("parse");
        System.out.println(subQuery);

        return new SubQuery("text", "simple", "%" + subQuery + "%");
    }
}
