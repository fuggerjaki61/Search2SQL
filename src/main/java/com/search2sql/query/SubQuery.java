package com.search2sql.query;

import com.search2sql.parser.Parser;

/**
 * This is a basic DTO (data-transfer-object) for transferring data between the second <code>Interpreting</code> and
 * the third phase <code>Translating</code>. This <code>SubQuery</code> is generated by a specific {@link Parser Parser}
 * in the first phase <code>Parsing</code>. This can also be generated by the {@link com.search2sql.interpreter.Interpreter Interpreter}
 * to add extra information. The
 * {@link Query} is just a wrapper to determine the order of the sub-queries.
 * <br><br>
 * <b>See Also</b><br>
 * <i>Overall Documentation</i><br>
 * {@link Query com.searchflow.query.Query}<br>
 * {@link Parser com.searchflow.parser.Parser}<br>
 * {@link com.search2sql.interpreter.Interpreter com.searchflow.interpreter.Interpreter}<br>
 * {@link com.search2sql.translator.Translator com.searchflow.translator.Translator}
 */
public class SubQuery {

    /**
     * This string contains information about the parser (its id).
     */
    private String parserId;

    /**
     * This string contains information how to handle this sub-query. A <code>Parser</code> may add how it should
     * be translated.
     * <br><br>
     * <b>Example</b><br>
     * {@link com.search2sql.impl.translator.FileTranslator}
     */
    private String type;

    /**
     * This object contains the basic value that was defined in the basic string form of the string expression.
     * This value will be later also found in the generated sql as the value.
     */
    private Object value;

    /**
     * This is a basic constructor that does nothing.
     */
    public SubQuery() {
        // NOOP
    }

    /**
     * This is a basic constructor that does nothing beside initializing values.
     *
     * @param parserId parser id
     * @param type extra information for translation
     * @param value parsed value
     */
    public SubQuery(String parserId, String type, Object value) {
        this.parserId = parserId;
        this.type = type;
        this.value = value;
    }

    /**
     * Gets parser id.
     *
     * @return the parser id
     */
    public String getParserId() {
        return parserId;
    }

    /**
     * Sets parser id.
     *
     * @param parserId the parser id
     */
    public void setParserId(String parserId) {
        this.parserId = parserId;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
