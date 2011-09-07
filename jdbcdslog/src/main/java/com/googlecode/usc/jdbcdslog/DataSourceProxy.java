package com.googlecode.usc.jdbcdslog;

import javax.sql.DataSource;

@SuppressWarnings("unchecked")
public class DataSourceProxy extends DataSourceProxyBase implements DataSource {

    private static final long serialVersionUID = -6888072076120346186L;

    public DataSourceProxy() throws JDBCDSLogException {
        super();
    }

}
