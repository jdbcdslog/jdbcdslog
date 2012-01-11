package org.jdbcdslog;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LogTextTest extends TestCase {
	public LogTextTest(String s) {
		super(s);
	}
	
	public static Test suite() {
		return new TestSuite(LogTextTest.class);
	}
	
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
