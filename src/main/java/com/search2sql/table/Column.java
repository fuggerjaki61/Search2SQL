package com.search2sql.table;

/**
 * This DAO contains basic information about the column used in the {@link TableConfig}. This is essential for the
 * <code>Interpreting</code> and <code>Translating</code> phase.<br>
 * This class basically contains three values:
 * <ul>
 *     <li>
 *         <code>name</code> - name of the column used in sql translation
 *     </li>
 *     <li>
 *         <code>parserId</code> - id of the parser that should be used when parsing the sub-query
 *     </li>
 *     <li>
 *         <code><span style="font-size: x-small;">boolean</span> primary</code> - indicates if this column should be used
 *         as default if sub-query is not identifiable in a multi-column table config
 *     </li>
 * </ul>
 */
public class Column {

    /**
     * This string contains the name of the column later used while translating the query to sql.
     */
    private String name;

    /**
     * This string contains the id of the parser that should be used while parsing.
     */
    private String parserId;

    /**
     * This boolean indicates whether to use this column as default if a sub-query is not identifiable for another in a
     * table with multiple columns.
     */
    private boolean primary;

    /**
     * <b>Important</b><br />
     * This constructors purpose is to minimize the parameter list by initializing the <code>primary</code> field with the
     * value <code>false</code> as default.
     * <br /><br />
     * <b>See Also</b><br />
     * {@link Column}<br />
     * {@link Column#Column(String, String, boolean)}<br />
     *
     * @param name     the name
     * @param parserId the parser id
     */
    public Column(String name, String parserId) {
        // passing the parameters on
        // primary is by default false
        this(name, parserId, false);
    }

    /**
     * This is a basic constructor just setting the fields' values by the parameters' values.
     * <br /><br />
     * <b>See Also</b><br />
     * {@link Column}<br />
     * {@link Column#Column(String, String)}<br />
     *
     * @param name     the name
     * @param parserId the parser id
     * @param primary  the primary
     */
    public Column(String name, String parserId, boolean primary) {
        // just sets values
        this.name = name;
        this.parserId = parserId;
        this.primary = primary;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
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
     * Is primary boolean.
     *
     * @return the boolean
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Sets primary.
     *
     * @param primary the primary
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
