package org.jdbcdslog;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.jdbcdslog.ConfigurationParameters;
import org.jdbcdslog.DriverLoggingProxy;
import org.junit.Test;

public class LogTextTest {
    @Test
    public void test() throws Exception {
        ConfigurationParameters.setLogText(true);
        DriverLoggingProxy proxy = new DriverLoggingProxy();
        Properties pr = new Properties();
        pr.put("user", "sa");
        Connection con = proxy.connect("jdbc:jdbcdslog:hsqldb:mem:mymemdb;targetDriver=org.hsqldb.jdbcDriver", pr);
        con.createStatement().execute("create table test_text (a varchar(10000))");
        PreparedStatement ps = con.prepareStatement("insert into test_text(a) values(?)");
        ps.setCharacterStream(1, new StringReader("hello"), 100);
        ps.execute();
        con.close();
    }
}
