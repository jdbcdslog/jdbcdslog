package org.jdbcdslog;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.hsqldb.jdbc.jdbcDataSource;
import org.jdbcdslog.ConnectionLoggingProxy;
import org.junit.Test;



public class ConnectionLoggingProxyTest  {

    @Test
    public void testConn() throws Exception {
        jdbcDataSource ds = new jdbcDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:mymemdb");
        ds.setUser("sa");
        Connection con = ds.getConnection();
        con = ConnectionLoggingProxy.wrap(con);
        con.createStatement().execute("create table test4 (a integer)");
        PreparedStatement ps = con.prepareStatement("insert into test4 values(?)");
        ps.setInt(1, 1);
        ps.execute();
        ps.close();
        con.close();
    }
}
