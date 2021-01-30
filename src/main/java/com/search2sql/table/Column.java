package com.search2sql.table;

/**
 * This class represents the value of a SQL column. This is the smallest structural unit and a set of these columns is
 * collected in a {@link Table}.
 * <br><br>
 * The column has a name that should be unique, unless you want a column to have multiple types like string and int. Each
 * column has the id of the parser that will be used for parsing.<br>
 * As a new feature <i>weight</i> is added. This integer defines how important this column is in the table. This can be used
 * for marking columns less relevant than others. How this behaves exactly is based on which implementation of the
 * {@link com.search2sql.interpreter.Interpreter Interpreter} you're using (it can also be ignored).
 *
 * @author fuggerjaki61
 * @since 2.0-zulu
 */
public class Column {

    private final String name;
    private final String parserId;
    private final int weight;

    /**
     * This is a basic constructor initializing values. No logic is performed.
     * <br><br>
     * <b>Note</b><br>
     * This constructor uses the default value <code>0</code> for the column weight.
     *
     * @param name name of the column
     * @param parserId id of the parser used
     */
    public Column(String name, String parserId) {
        this(name, parserId, 0);
    }

    /**
     * This is a basic constructor initializing values. No logic is performed.
     *
     * @param name name of the column
     * @param parserId id of the parser used
     * @param weight importance of this column
     */
    public Column(String name, String parserId, int weight) {
        this.name = name;
        this.parserId = parserId;
        this.weight = weight;
    }

    /**
     * Returns the name of the column.
     *
     * @return name of the column
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the id of the parser that is used for parsing this column.
     *
     * @return id of the parser used
     */
    public String getParserId() {
        return parserId;
    }

    /**
     * Returns the relevance of the column.
     *
     * @return importance of the column
     */
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", parserId='" + parserId + '\'' +
                ", weight=" + weight +
                '}';
    }
}
