package com.search2sql.table;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the value of a SQL table. A set of these tables is collected in the {@link TableConfig}.
 * <br><br>
 * The table has a prefix that will be added in front of the column name to prevent column name duplication (your SQL
 * query has to support this) and a list of {@link Column}s.
 * <br><br>
 * <b>Important</b><br>
 * When using multiple tables, each table should specify an unique prefix.
 *
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public class Table {

    private final String prefix;
    private final Set<Column> columns;

    /**
     * This is a basic constructor. This constructor uses the default prefix <code>null</code>.<br>
     * If you're using multiple tables, you should add a prefix.
     *
     * @param columns set of columns of this table
     */
    public Table(Column... columns) {
        this(null, columns);
    }

    /**
     * This is a basic constructor. This constructor uses the default prefix <code>null</code>.<br>
     * If you're using multiple tables, you should add a prefix.
     *
     * @param columns set of columns of this table
     */
    public Table(Set<Column> columns) {
        this(null, columns);
    }

    /**
     * This is a basic constructor for enhanced usage.
     *
     * @param prefix prefix of the table
     * @param columns set of columns of this table
     */
    public Table(String prefix, Column... columns) {
        this(prefix,  new HashSet<>(Arrays.asList(columns)));
    }

    /**
     * This is a basic constructor performing no logic.
     *
     * @param prefix prefix of the table
     * @param columns set of columns of this table
     */
    public Table(String prefix, Set<Column> columns) {
        this.prefix = prefix;
        this.columns = Collections.unmodifiableSet(columns);
    }

    /**
     * This method returns the prefix that was set via the constructor. If the value wasn't set, <code>null</code> is
     * returned as a default.
     *
     * @return prefix of the table
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * This method returns the unmodifiable set of all columns in this table.
     *
     * @return unmodifiable set of all columns
     */
    public Set<Column> getColumns() {
        return columns;
    }
}
