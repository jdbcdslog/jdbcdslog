package org.jdbcdslog;

import javax.sql.DataSource;
import javax.sql.XADataSource;

public class XADataSourceProxy extends DataSourceProxyBase implements XADataSource, DataSource {
	
	public XADataSourceProxy() throws JDBCDSLogException {
		super();
	}

}
