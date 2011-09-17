package org.jdbcdslog;

public class JDBCDSLogException extends Exception {
    private static final long serialVersionUID = 2791270426551839139L;

    public JDBCDSLogException(String s) {
        super(s);
    }

    public JDBCDSLogException(Throwable e) {
        super(e);
    }
}
