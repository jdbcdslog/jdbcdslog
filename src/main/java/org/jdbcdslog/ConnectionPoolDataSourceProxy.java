package org.jdbcdslog;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

public class ConnectionPoolDataSourceProxy extends DataSourceProxyBase implements DataSource
	, ConnectionPoolDataSource {

	public ConnectionPoolDataSourceProxy() throws JDBCDSLogException {
		super();
	}

}
