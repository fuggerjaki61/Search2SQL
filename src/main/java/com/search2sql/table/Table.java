package com.search2sql.table;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public class Table {

    private final String prefix;
    private final Set<Column> columns;

    public Table(Column... columns) {
        this(null, columns);
    }

    public Table(Set<Column> columns) {
        this(null, columns);
    }

    public Table(String prefix, Column... columns) {
        this(prefix,  new HashSet<>(Arrays.asList(columns)));
    }

    public Table(String prefix, Set<Column> columns) {
        this.prefix = prefix;
        this.columns = columns;
    }

    public String getPrefix() {
        return prefix;
    }

    public Set<Column> getColumns() {
        return columns;
    }
}
