package org.jdbcdslog;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.hsqldb.jdbc.jdbcDataSource;
import org.junit.Test;

public class ConnectionStressTest  {
    @Test
    public void test() throws Exception {
        jdbcDataSource ds = new jdbcDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:mymemdb");
        ds.setUser("sa");
        Connection con = ds.getConnection();
        con.createStatement().execute("create table test3 (a integer)");
        PreparedStatement ps = con.prepareStatement("insert into test3 values(?)");
        for (int i = 0; i < 10000; i++) {
            ps.setInt(1, i);
            ps.execute();
        }
        ps.close();
        con.close();
    }
}
