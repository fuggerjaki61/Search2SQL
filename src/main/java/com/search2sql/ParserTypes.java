package com.search2sql;

/**
 * This class contains a list of the id's of the current available {@link com.search2sql.parser.Parser Parsers}.
 * <br><br>
 * <b>Example</b><br>
 * <code>new Column("someName", ParserTypes.INT);</code>
 */
public class ParserTypes {

    /**
     * This is the parser id for the {@link com.search2sql.impl.parser.DateParser} implementation.
     * <br><br>
     * This parser can be used for parsing dates.
     */
    public static final String DATE = "default.date";

    /**
     * This is the parser id for the {@link com.search2sql.impl.parser.IntParser} implementation.
     * <br><br>
     * This parser can be used for parsing integers.
     */
    public static final String INT = "default.int";

    /**
     * This is the parser id for the {@link com.search2sql.impl.parser.TextParser} implementation.
     * <br><br>
     * This parser can be used for parsing any text or even quotes.
     */
    public static final String TEXT = "default.text";
}
