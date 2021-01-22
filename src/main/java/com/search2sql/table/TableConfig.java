package com.search2sql.table;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is class contains the whole configuration for a table complex.
 * <br><br>
 * This class contains a set of {@link Table Tables} which again contain {@link Column Columns}. This class just functions
 * as a wrapper for the set of tables. As a new feature multiple tables are supported.
 * <br><br>
 * <b>Important</b><br>
 * If you're using multiple tables, each table should a have an unique table prefix to prevent column name duplication.
 *
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public class TableConfig {

    private final Set<Table> tables;

    /**
     * This is a basic constructor for enhanced usage. No logic is performed.
     *
     * @param tables set of tables
     */
    public TableConfig(Table... tables) {
        // call the constructor with a new set
        this(new HashSet<>(Arrays.asList(tables)));
    }

    /**
     * This is a basic constructor just setting the value. No logic is performed.
     *
     * @param tables set of tables
     */
    public TableConfig(Set<Table> tables) {
        // create a new unmodifiable set
        this.tables = Collections.unmodifiableSet(tables);
    }

    /**
     * This method returns an unmodifiable set with all tables that were set when the constructor was called. No changes
     * can be made.
     *
     * @return unmodifiable set of all tables
     */
    public Set<Table> getTables() {
        return tables;
    }
}
