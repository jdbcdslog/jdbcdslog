package org.jdbcdslog;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

public class ConnectionPoolXADataSourceProxy extends DataSourceProxyBase implements DataSource, XADataSource, ConnectionPoolDataSource {

    private static final long serialVersionUID = 5829721261280763559L;

    public ConnectionPoolXADataSourceProxy() throws JDBCDSLogException {
        super();
    }

}
