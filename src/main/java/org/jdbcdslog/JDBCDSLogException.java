package org.jdbcdslog;

public class JDBCDSLogException extends Exception {
	public JDBCDSLogException(String s) {
		super(s);
	}
	
	public JDBCDSLogException(Throwable e) {
		super(e);
	}
}
