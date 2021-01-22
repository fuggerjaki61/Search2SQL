package com.search2sql.table;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public class TableConfig {

    private final Set<Table> tables;

    public TableConfig(Table... tables) {
        this(new HashSet<>(Arrays.asList(tables)));
    }

    public TableConfig(Set<Table> tables) {
        this.tables = tables;
    }

    public Set<Table> getTables() {
        return tables;
    }
}
