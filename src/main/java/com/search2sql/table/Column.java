package com.search2sql.table;

/**
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public class Column {

    private final String name;
    private final String parserId;
    private final int weight;

    public Column(String name, String parserId) {
        this(name, parserId, 0);
    }

    public Column(String name, String parserId, int weight) {
        this.name = name;
        this.parserId = parserId;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getParserId() {
        return parserId;
    }

    public int getWeight() {
        return weight;
    }
}
