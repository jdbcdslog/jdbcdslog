package org.jdbcdslog;

import java.lang.reflect.Proxy;
import java.sql.Connection;

public class ConnectionLoggingProxy {

    public static Connection wrap(Connection con) {
        return (Connection) Proxy.newProxyInstance(con.getClass().getClassLoader(), new Class[] { Connection.class }, new GenericLoggingHandler(con));
    }

}
