package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked","rawtypes"})
public class CallableStatementLoggingProxy extends PreparedStatementLoggingProxy implements InvocationHandler {

    static Logger logger = LoggerFactory.getLogger(CallableStatementLoggingProxy.class);

    Map namedParameters = new TreeMap();

    public CallableStatementLoggingProxy(CallableStatement ps, String sql) {
        super(ps, sql);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = "invoke() ";
        if (logger.isDebugEnabled())
            logger.debug(methodName + "method = " + method);
        Object r = null;
        try {
            boolean toLog = (StatementLogger.isInfoEnabled() || SlowQueryLogger.isInfoEnabled()) && executeMethods.contains(method.getName());
            long t1 = 0;
            if (toLog)
                t1 = System.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug(methodName + "before method call..");
            r = method.invoke(target, args);
            if (logger.isDebugEnabled())
                logger.debug(methodName + "after method call. result = " + r);
            if (setMethods.contains(method.getName()) && args[0] instanceof Integer)
                parameters.put(args[0], args[1]);
            if (setMethods.contains(method.getName()) && args[0] instanceof String)
                namedParameters.put(args[0], args[1]);
            if ("clearParameters".equals(method.getName()))
                parameters = new TreeMap();
            if (toLog) {
                long t2 = System.currentTimeMillis();
                long time = t2 - t1;

                StringBuffer s = LogUtils.createLogEntry(method, sql, parametersToString(), namedParameters.toString());

                if (ConfigurationParameters.showTime) {
                    s.append(" ").append(t2 - t1).append(" ms.");
                }

                StatementLogger.info(s.toString());

                if (time >= ConfigurationParameters.slowQueryThreshold) {
                    SlowQueryLogger.info(s.toString());
                }
            }
            if (r instanceof ResultSet)
                r = ResultSetLoggingProxy.wrapByResultSetProxy((ResultSet) r);
        } catch (Throwable t) {
            LogUtils.handleException(t, StatementLogger.getLogger(), LogUtils.createLogEntry(method, sql, parametersToString(), namedParameters.toString()));
        }
        return r;
    }

}
