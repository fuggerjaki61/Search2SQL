package com.search2sql.parser;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.query.SubQuery;

/**
* This class is the main part of the first phase <code>Parsing</code>.<br>
* In this phase and the <code>Interpreting</code> phase the search query is split into its sub-queries and then they
* are parsed one by one into the {@link SubQuery} class and merged together in the {@link com.search2sql.query.Query Query}
* class. When creating a custom <code>Parser</code> nothing else than parsing must be implemented.
* <br><br>
 * This class may be extended by all classes that wish to be a <code>Parser</code>.<br>
 * <br><br>
 * <blockquote style="border-left: 2px solid #4edcff; padding-left: 5px;">
 *  <b>Important</b><br>
 *  Every <code>Parser</code> <i>must</i> have the {@link SearchParser @SearchParser} annotation and
 *  also a unique id <i>must</i> be specified.
 * </blockquote>
 * <br>
 * The parser got two basic methods {@link Parser#isParserFor(String)} and {@link Parser#parse(String)}.
 * For more information for <code>isParserFor(String)</code> see its documentation.
 * <br><br>
 * The <code>parse(String)</code> method takes the simple string form and parses it to the more complex form
 * of a {@link SubQuery}.
 * <br><br>
 * <b>See Also</b><br>
 * {@link com.search2sql.query.Query com.searchflow.query.Query}<br>
 * {@link SubQuery com.searchflow.query.SubQuery}<br>
 * {@link com.search2sql.interpreter.Interpreter com.searchflow.interpreter.Interpreter}
 * <br><br>
 * <b>Known Implementations</b><br>
 * {@link com.search2sql.impl.parser.TextParser com.searchflow.impl.parser.TextParser}<br>
 * {@link com.search2sql.impl.parser.IntParser com.searchflow.impl.parser.IntParser}
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
public abstract class Parser {

    /**
     * This method takes the split sub-query as parameter and checks if this sub-query can be parsed by this parser.
     * This method should return <code>true</code> if it is parsable and <code>false</code> when not.
     * <br><br>
     * This method is used in the <code>Interpreting</code> phase to check which <code>Parser</code> should be used while
     * parsing the current sub-query.
     * <br><br>
     * <b>Note</b><br>
     * When you created multiple, custom <code>Parser</code> in your project multiple parsers are allowed to return <code>true</code>
     * for the same sub-query.
     *
     * @param subQuery split part of the whole search query
     * @return boolean indicating if this parser can be used to parse this sub-query
     * @throws InvalidSearchException thrown if a problem occurred while parsing
     */
    public abstract boolean isParserFor(String subQuery) throws InvalidSearchException;

    /**
     * This method is essential for the first phase <code>Parsing</code>.
     * <br><br>
     * This method is called after this <code>Parser</code> was chosen to parse the current sub-query.
     * {@link Parser#isParserFor(String)} has been called before to check which one to choose.<br>
     * This method should parse the correct representation of the basic string form in the form of the more complex
     * {@link SubQuery}. This method mustn't do anything beside parsing the string. The rest will be done by the
     * {@link com.search2sql.interpreter.Interpreter Interpreter}.
     * <br><br>
     * For more information you can check the <i>overall documentation</i> or the documentation of this class.
     *
     * @param subQuery split part of the whole search query
     * @return parsed, more complex form of this sub-query
     * @throws InvalidSearchException thrown if a problem occurred while parsing
     */
    public abstract SubQuery parse(String subQuery) throws InvalidSearchException;
}
