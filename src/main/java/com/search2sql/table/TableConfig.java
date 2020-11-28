package com.search2sql.table;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the configuration of the specific table that is searchable. This DAO is used to hold the list
 * of {@link Column Columns} used in the table. This specifies the table that is shown to the user and the part or whole
 * server-side table. This is used in the <code>Interpreting</code> phase to check which sub-query is for which column and
 * to check which <code>Parsers</code> can be used. Also this is used in the <code>Translating</code> phase to check how
 * to name the columns and to know how the table looks like.
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
public class TableConfig {

    /**
     * This defines the name of the table used while translating the search.
     */
    private final String name;

    /**
     * This list saves the config of each column. This is the most important data in this class.
     * <br><br>
     * <b>See Also</b><br>
     * {@link Column com.searchflow.table.Column}
     */
    private final ArrayList<Column> columns;

    /**
     * This is a basic constructor that does nothing.
     */
    public TableConfig() {
        name = null;
        columns = new ArrayList<>();
    }

    /**
     * This is a basic constructor that does nothing beside initializing values.
     *
     * @param columns the columns
     */
    public TableConfig(Column... columns) {
        this(new ArrayList<>(Arrays.asList(columns)));
    }

    /**
     * This is a basic constructor that does nothing beside initializing values.
     *
     * @param columns the columns
     */
    public TableConfig(ArrayList<Column> columns) {
        name = null;
        this.columns = columns;
    }

    /**
     * This is a basic constructor that does nothing beside initializing values.
     *
     * @param name    the name
     * @param columns the columns
     */
    public TableConfig(String name, Column... columns) {
        this(name, new ArrayList<>(Arrays.asList(columns)));
    }

    /**
     * This is a basic constructor that does nothing beside initializing values.
     *
     * @param name    the name
     * @param columns the columns
     */
    public TableConfig(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;
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
     * Gets columns.
     *
     * @return the columns
     */
    public ArrayList<Column> getColumns() {
        return columns;
    }
}
