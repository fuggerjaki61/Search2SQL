package com.search2sql.impl.translator;

import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.translator.Translator;

public class BasicTranslator extends Translator {

    @Override
    public String translate(Query query) {
        StringBuilder sql = new StringBuilder();

        for (SubQuery subQuery : query.getSubQueries()) {
            if ("text".equals(subQuery.getParserId())) {
                sql.append(query.getTableConfig().getName());

                sql.append(".");

                sql.append("textColumn"); // TODO need a way to resolve column name

                sql.append(" = '");

                sql.append(subQuery.getValue());

                sql.append("'");

                sql.append(" ");
            } else if ("int".equals(subQuery.getParserId()))  {
                sql.append(query.getTableConfig().getName());

                sql.append(".");

                sql.append("intColumn"); // TODO need a way to resolve column name

                sql.append(" = ");

                sql.append(subQuery.getValue());

                sql.append(" ");
            } else {
                sql.append("OR ");
            }
        }

        return sql.toString().trim();
    }

    @Override
    public String translateWithValue(Query query) {
        throw new UnsupportedOperationException();
    }
}
