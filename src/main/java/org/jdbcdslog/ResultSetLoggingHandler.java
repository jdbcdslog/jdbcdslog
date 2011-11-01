package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ResultSetLoggingHandler implements InvocationHandler {
    Object target = null;

    public ResultSetLoggingHandler(ResultSet target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object r = null;
        try {
            r = method.invoke(target, args);
        } catch (Throwable e) {
            LogUtils.handleException(e, ResultSetLogger.getLogger(), LogUtils.createLogEntry(method, null, null, null));
        }
        if (ResultSetLogger.isInfoEnabled() && method.getName().equals("next") && ((Boolean) r).booleanValue()) {
            String fullMethodName = method.getDeclaringClass().getName() + "." + method.getName();
            ResultSet rs = (ResultSet) target;
            ResultSetMetaData md = rs.getMetaData();
            StringBuffer s = new StringBuffer(fullMethodName).append(" {");
            if (md.getColumnCount() > 0)
                s.append(ConfigurationParameters.rdbmsSpecifics.formatParameter(rs.getObject(1)));
            for (int i = 2; i <= md.getColumnCount(); i++)
                s.append(", ").append(ConfigurationParameters.rdbmsSpecifics.formatParameter(rs.getObject(i)));
            s.append("}");

            ResultSetLogger.info(s.toString());
        }
        return r;
    }

    static Object wrapByResultSetProxy(ResultSet r) {
        return Proxy.newProxyInstance(r.getClass().getClassLoader(), new Class[] { ResultSet.class }, new ResultSetLoggingHandler(r));
    }

}
