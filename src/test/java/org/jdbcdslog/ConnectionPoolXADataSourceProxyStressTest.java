package org.jdbcdslog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConnectionPoolXADataSourceProxyStressTest extends TestCase {
	public ConnectionPoolXADataSourceProxyStressTest(String name) {
		super(name);
	}
	
	public static Test suite() {
		return new TestSuite(ConnectionPoolXADataSourceProxyStressTest.class);
	}
	
	public void test() throws Exception {
		ConnectionPoolXADataSourceProxy ds = new ConnectionPoolXADataSourceProxy();
		ds.setDatabase("jdbc:hsqldb:mem:mymemdb;targetDS=org.hsqldb.jdbc.jdbcDataSource");
		ds.setUser("sa");
		Connection con = ds.getConnection();
		con.createStatement().execute("create table test2 (a integer)");
		PreparedStatement ps = con.prepareStatement("insert into test2 values(?)");
		for(int i = 0; i < 10000; i ++) {
			ps.setInt(1, i);
			ps.execute();
		}
		ps.close();
		con.close();
	}
}
