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
        return super.isParserFor(subQuery) || subQuery.matches("^\\S+$");
    }

    @Override
    public SubQuery parse(String subQuery) {
        if (super.isParserFor(subQuery)) {
            String processed = subQuery;

            processed = processed.replaceAll("^\\s*" + getQuotation(), "");
            processed = processed.replaceAll(getQuotation() + "\\s*$", "");

            return new SubQuery("default.text", "quote", "%" + processed + "%");
        }

        return new SubQuery("default.text", "simple", "%" + subQuery + "%");
    }
}
