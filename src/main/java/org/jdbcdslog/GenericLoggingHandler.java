package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@SuppressWarnings("rawtypes")
public class GenericLoggingHandler implements InvocationHandler {
    String sql = null;

    Object target = null;

    public GenericLoggingHandler(Object target) {
        this.target = target;
    }

    public GenericLoggingHandler(Object target, String sql) {
        this.target = target;
        this.sql = sql;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        try {
            Object r = method.invoke(target, args);
            if (method.getName().equals("prepareCall") || method.getName().equals("prepareStatement"))
                r = wrap(r, (String) args[0]);
            else
                r = wrap(r, null);
            return r;
        } catch (Throwable t) {
            LogUtils.handleException(t, ConnectionLogger.getLogger(), LogUtils.createLogEntry(method, null, null, null));
        }
        return null;
    }

    private Object wrap(Object r, String sql) throws Exception {
        if (r instanceof Connection) {
            Connection con = (Connection) r;
            if (ConnectionLogger.isInfoEnabled())
                ConnectionLogger.info("connect to URL " + con.getMetaData().getURL() + " for user " + con.getMetaData().getUserName());
            return wrapByGenericProxy(r, Connection.class, sql);
        }
        if (r instanceof CallableStatement)
            return wrapByCallableStatementProxy(r, sql);
        if (r instanceof PreparedStatement)
            return wrapByPreparedStatementProxy(r, sql);
        if (r instanceof Statement)
            return wrapByStatementProxy(r);
        if (r instanceof ResultSet)
            return ResultSetLoggingHandler.wrapByResultSetProxy((ResultSet) r);
        return r;
    }

    private Object wrapByStatementProxy(Object r) {
        return Proxy.newProxyInstance(r.getClass().getClassLoader(), new Class[] { Statement.class }, new StatementLoggingHandler((Statement) r));
    }

    private Object wrapByPreparedStatementProxy(Object r, String sql) {
        return Proxy.newProxyInstance(r.getClass().getClassLoader(), new Class[] { PreparedStatement.class }, new PreparedStatementLoggingHandler((PreparedStatement) r, sql));
    }

    private Object wrapByCallableStatementProxy(Object r, String sql) {
        return Proxy.newProxyInstance(r.getClass().getClassLoader(), new Class[] { CallableStatement.class }, new CallableStatementLoggingHandler((CallableStatement) r, sql));
    }

    static Object wrapByGenericProxy(Object r, Class interf, String sql) {
        return Proxy.newProxyInstance(r.getClass().getClassLoader(), new Class[] { interf }, new GenericLoggingHandler(r, sql));
    }

}
