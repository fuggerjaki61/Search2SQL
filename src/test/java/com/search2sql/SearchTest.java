package com.search2sql;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class SearchTest {

    @Test
    void test() throws SQLException, InvalidSearchException {
        Search search = new SearchBuilder()
                .setTableConfig(new TableConfig(new Column("text", "text")))
                .build();

        PreparedStatement ps = search.prepareStatement("abc test", new TestConnection(), "", "", 1);

        System.out.println(ps);
    }

}