package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class StatementLoggingProxy implements InvocationHandler {

    static Logger logger = LoggerFactory.getLogger(StatementLoggingProxy.class);

    Object targetStatement = null;

    @SuppressWarnings("rawtypes")
    static List executeMethods = Arrays.asList(new String[] { "addBatch", "execute", "executeQuery", "executeUpdate" });

    public StatementLoggingProxy(Statement statement) {
        targetStatement = statement;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object r = null;
        try {
            boolean toLog = (StatementLogger.isInfoEnabled() || SlowQueryLogger.isInfoEnabled()) && executeMethods.contains(method.getName());
            long t1 = 0;
            if (toLog)
                t1 = System.currentTimeMillis();
            r = method.invoke(targetStatement, args);
            if (r instanceof ResultSet)
                r = ResultSetLoggingProxy.wrapByResultSetProxy((ResultSet) r);
            if (toLog) {
                long t2 = System.currentTimeMillis();
                StringBuffer sb = LogUtils.createLogEntry(method, args == null ? null : args[0], null, null);
                long time = t2 - t1;

                if (ConfigurationParameters.showTime) {
                    sb.append(" ").append(t2 - t1).append(" ms.");
                }

                StatementLogger.info(sb.toString());

                if (time >= ConfigurationParameters.slowQueryThreshold) {
                    SlowQueryLogger.info(sb.toString());
                }

            }
        } catch (Throwable t) {
            LogUtils.handleException(t, StatementLogger.getLogger(), LogUtils.createLogEntry(method, args[0], null, null));
        }
        return r;
    }

}
