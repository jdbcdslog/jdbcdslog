package org.jdbcdslog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import org.jdbcdslog.DriverLoggingProxy;
import org.junit.Test;

public class DriverLoggingProxyTest{
    @Test
    public void test() throws Exception {
        DriverLoggingProxy proxy = new DriverLoggingProxy();
        Properties pr = new Properties();
        pr.put("user", "sa");
        Connection con = proxy.connect("jdbc:jdbcdslog:hsqldb:mem:mymemdb;targetDriver=org.hsqldb.jdbcDriver", pr);
        con.createStatement().execute("create table test_dr (a integer)");
        con.createStatement().execute("insert into test_dr values(1)");
        ResultSet rs = con.createStatement().executeQuery("select * from test_dr");
        rs.close();
        con.close();
    }
}
