package com.search2sql.impl.translator;

import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.translator.Translator;

/**
 * This is a very basic implementation of {@link Translator}.<br />
 * This implementation was just added for a little demo of the capabilities of this library.
 * <br /><br />
 * <b>Note</b><br />
 * Use the {@link FileTranslator} if you want to experience the full power of this API.
 *
 * @author fuggerjaki61
 * @since 0.0.1
 */
@Deprecated
public class BasicTranslator extends Translator {

    /**
     * <b>Note</b><br />
     * As already said in the documentation of this class (see {@link BasicTranslator}) this is just a small demo.
     * For this reason the <code>translate()</code> method does NOT replace the value with a <code>?</code> (question mark)
     * it will add them as plain text.
     * <br /><br />
     * Normally this method would replace all sql values with <code>?</code> (question marks). For testing purposes this
     * method inserts the values as <i>plain</i> text.
     *
     * @param query parsed & interpreted version of the basic string expression
     * @return sql query WITH values inserted
     */
    @Override
    public String translate(Query query) {
        // new sql query builder
        StringBuilder sql = new StringBuilder();

        // iterate over every sub-query
        for (SubQuery subQuery : query.getSubQueries()) {
            // checks if parser is a TextParser
            if ("text".equals(subQuery.getParserId()) || "date".equals(subQuery.getParserId()) || "quoted".equals(subQuery.getParserId())) {
                // comments describe how the query looks at which stage

                // "tableName"
                sql.append(query.getTableConfig().getName());

                // "tableName."
                sql.append(".");

                // "tableName.columnName"
                sql.append(subQuery.getColumnName());

                // "tableName.columnName = '"
                sql.append(" = '");

                // "tableName.columnName = 'someValue"
                sql.append(subQuery.getValue());

                // "tableName.columnName = 'someValue'"
                sql.append("'");

                // "tableName.columnName = 'someValue' "
                sql.append(" ");

                //checks if parser is an IntParser
            } else if ("int".equals(subQuery.getParserId()))  {
                // comments describe how the query looks at which stage

                // "tableName"
                sql.append(query.getTableConfig().getName());

                // "tableName."
                sql.append(".");

                // "tableName.columnName"
                sql.append(subQuery.getColumnName());

                // "tableName.columnName = "
                sql.append(" = ");

                // "tableName.columnName = someValue"
                sql.append(subQuery.getValue());

                // "tableName.columnName = someValue "
                sql.append(" ");

                // checks if query is a logical 'OR'
            } else {
                // adds 'OR ' to query
                sql.append("OR ");
            }
        }

        // trims string because it still contains whitespaces at the end
        // returns finished sql query
        return sql.toString().trim();
    }
}
