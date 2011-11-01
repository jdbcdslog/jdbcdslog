package org.jdbcdslog;

import java.lang.reflect.Proxy;

import javax.sql.PooledConnection;

public class PooledConnectionLoggingProxy {

    public static PooledConnection wrap(PooledConnection con) {
        return (PooledConnection) Proxy.newProxyInstance(con.getClass().getClassLoader(), new Class[] { PooledConnection.class }, new GenericLoggingHandler(con));
    }

}
