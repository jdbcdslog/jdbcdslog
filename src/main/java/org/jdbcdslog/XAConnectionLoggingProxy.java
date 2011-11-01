package org.jdbcdslog;

import java.lang.reflect.Proxy;

import javax.sql.XAConnection;

public class XAConnectionLoggingProxy {

    public static XAConnection wrap(XAConnection con) {
        return (XAConnection) Proxy.newProxyInstance(con.getClass().getClassLoader(), new Class[] { XAConnection.class }, new GenericLoggingHandler(con));
    }

}
