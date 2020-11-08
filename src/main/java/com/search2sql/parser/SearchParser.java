package com.search2sql.parser;

import java.lang.annotation.*;

/**
 * This annotation is part of the first phase <code>Parsing</code>.<br>
 * This annotation is used to mark the available {@link Parser Parsers} to the {@link com.search2sql.interpreter.Interpreter Interpreter}
 * used in the second phase <code>Interpreting</code>.<br>
 * For more information see the documentation of {@link Parser com.searchflow.parser.Parser}.
 * <br><br>
 * <blockquote style="border-left: 2px solid #4edcff; padding-left: 5px;">
 *  <b>Important</b><br>
 *  All classes using this annotation should also extend the {@link Parser} class.
 * </blockquote>
 * <br>
 * This annotation has one required attribute <code>value</code>. This attribute specifies the id of the <code>Parser</code>.
 * <br><br>
 * <blockquote style="border-left: 2px solid #4edcff; padding-left: 5px;">
 *  <b>Important</b><br>
 *  The id of every <code>Parser</code> must be unique.
 * </blockquote>
 * <br>
 * <b>Note</b><br>
 * The id should be created with this template:<br>
 * <blockquote><code>[projectName].[parserName]</code></blockquote>
 * The default implementations just specify a <code>parserName</code>.
 * <br><br>
 * <b>See Also</b><br>
 * <i>Overall Documentation</i><br>
 * {@link Parser com.searchflow.parser.Parser}<br>
 * {@link com.search2sql.interpreter.Interpreter}
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SearchParser {

    /**
     * This attribute specifies the id of the <code>Parser</code>.<br>
     * <br>
     * <blockquote style="border-left: 2px solid #4edcff; padding-left: 5px;">
     *  <b>Important</b><br>
     *  The id of every <code>Parser</code> must be unique.
     * </blockquote>
     * <br>
     * As already mentioned in the documentation of this class the id should be created with this template:<br>
     * <blockquote><code>[projectName].[parserName]</code></blockquote>
     * The default implementations just specify a <code>parserName</code>.
     * <br><br>
     * <b>Note</b><br>
     * The name of this attribute is chosen because <code>value</code> is the default attribute name and the value can
     * be set without extra specifying its name.
     *
     * @return id of the parser
     */
    String value();
}
