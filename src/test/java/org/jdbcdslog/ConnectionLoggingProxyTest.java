package org.jdbcdslog;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.hsqldb.jdbc.jdbcDataSource;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConnectionLoggingProxyTest extends TestCase {
	public ConnectionLoggingProxyTest(String name) {
		super(name);
	}
	
	public static Test suite() {
		return new TestSuite(ConnectionLoggingProxyTest.class);
	}
	
	public void test() throws Exception {
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
