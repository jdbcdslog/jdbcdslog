package org.jdbcdslog;

import java.sql.Connection;
import java.sql.ResultSet;

import org.hsqldb.jdbc.jdbcDataSource;
import org.jdbcdslog.ConnectionPoolXADataSourceProxy;
import org.junit.Test;

public class ConnectionPoolXADataSourceProxyTest  {
    @Test
    public void testConnectionPoolXADataSource() throws Exception {
        ConnectionPoolXADataSourceProxy ds = new ConnectionPoolXADataSourceProxy();
        ds.setDatabase("jdbc:hsqldb:mem:mymemdb;targetDS=org.hsqldb.jdbc.jdbcDataSource");
        ds.setUser("sa");
        Connection con = ds.getConnection();
        con.createStatement().execute("create table test (a integer)");
        con.createStatement().execute("insert into test values(1)");
        ResultSet rs = con.createStatement().executeQuery("select * from test");
        rs.close();
        con.close();
    }
    @Test
    public void testSetTartetDS() throws Exception {
        ConnectionPoolXADataSourceProxy ds = new ConnectionPoolXADataSourceProxy();
        ds.setDatabase("jdbc:hsqldb:mem:mymemdb");
        ds.setTargetDS("org.hsqldb.jdbc.jdbcDataSource");
        ds.setUser("sa");
        Connection con = ds.getConnection();
        con.createStatement().execute("create table test5 (a integer)");
        con.createStatement().execute("insert into test5 values(1)");
        ResultSet rs = con.createStatement().executeQuery("select * from test5");
        rs.close();
        con.close();
    }
    @Test
    public void testSetTargetDSDirect() throws Exception {
        org.hsqldb.jdbc.jdbcDataSource ds = new jdbcDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:mymemdb");
        ds.setUser("sa");
        ConnectionPoolXADataSourceProxy ds2 = new ConnectionPoolXADataSourceProxy();
        ds2.setTargetDSDirect(ds);
        Connection con = ds2.getConnection();
        con.createStatement().execute("create table test6 (a integer)");
        con.createStatement().execute("insert into test6 values(1)");
        ResultSet rs = con.createStatement().executeQuery("select * from test6");
        rs.close();
        con.close();
    }
}
