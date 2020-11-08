package com.search2sql.query;

import com.search2sql.table.TableConfig;

import java.util.LinkedList;

/**
 * This is basic DTO used for the transfer between the {@link com.search2sql.interpreter.Interpreter Interpreter} and
 * {@link com.search2sql.translator.Translator Translator} - or between the <code>Interpreting</code> phase and the
 * <code>Translating</code> phase.
 * <br><br>
 * This class saves the original string search query, the meta-information about the table layout and a list of the split
 * {@link SubQuery SubQueries}. In the <code>Interpreting</code> phase the original string search query was split into
 * its smallest parts and saved in a list.
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
public class Query {

    /**
     * This is the <i>original string search query</i>.<br>
     * This is the string that was entered by the user at the beginning.
     */
    private String original;

    /**
     * This is the <i>meta-information about the table layout</i>.<br>
     * This is later gonna be used in the <code>Translating</code> phase.
     */
    private TableConfig tableConfig;

    /**
     * This is the <i>parsed and interpreted, split sub-queries</i>.<br>
     * This list was generated in the <code>Interpreting</code> phase and is the most important information
     * transmitted.
     */
    private LinkedList<SubQuery> subQueries;

    /**
     * Basic constructor that does nothing beside initializing an empty list.
     */
    public Query() {
        // just initializes list
        subQueries = new LinkedList<>();
    }

    /**
     * Basic constructor meant to provide a shortcut that initializes an empty list of sub-queries.
     *
     * @param original original string search query
     * @param tableConfig meta-information about the table layout
     */
    public Query(String original, TableConfig tableConfig) {
        // passes values on and initializes list
        this(original, tableConfig, new LinkedList<>());
    }

    /**
     * Basic constructor that does nothing beside initializing this object's attributes.
     *
     * @param original original string search query
     * @param tableConfig meta-information about the table layout
     * @param subQueries parsed and interpreted, split sub-queries
     */
    public Query(String original, TableConfig tableConfig, LinkedList<SubQuery> subQueries) {
        this.original = original;
        this.tableConfig = tableConfig;
        this.subQueries = subQueries;
    }

    /**
     * This method just provides a shortcut to add SubQueries more easily.<br>
     * This method can just be used and no list must be added.
     *
     * @param query SubQuery that should be added to list
     */
    public void addSubQuery(SubQuery query) {
        // just adds query
        subQueries.add(query);
    }

    /**
     * Gets original.
     *
     * @return the original
     */
    public String getOriginal() {
        return original;
    }

    /**
     * Sets original.
     *
     * @param original the original
     */
    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     * Gets table config.
     *
     * @return the table config
     */
    public TableConfig getTableConfig() {
        return tableConfig;
    }

    /**
     * Sets table config.
     *
     * @param tableConfig the table config
     */
    public void setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }

    /**
     * Gets sub queries.
     *
     * @return the sub queries
     */
    public LinkedList<SubQuery> getSubQueries() {
        return subQueries;
    }

    /**
     * Sets sub queries.
     *
     * @param subQueries the sub queries
     */
    public void setSubQueries(LinkedList<SubQuery> subQueries) {
        this.subQueries = subQueries;
    }
}
