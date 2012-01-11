package org.jdbcdslog;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

public class ConnectionPoolXADataSourceProxy extends DataSourceProxyBase implements DataSource, XADataSource
	, ConnectionPoolDataSource {

	public ConnectionPoolXADataSourceProxy() throws JDBCDSLogException {
		super();
	}

}
