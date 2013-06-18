package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class StatementLoggingHandler implements InvocationHandler {
    Object targetStatement = null;

    @SuppressWarnings("rawtypes")
    static List executeMethods = Arrays.asList(new String[] { "addBatch", "execute", "executeQuery", "executeUpdate" });

    public StatementLoggingHandler(Statement statement) {
        targetStatement = statement;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object r = null;
        try {
            boolean toLog = (StatementLogger.isInfoEnabled() || SlowQueryLogger.isInfoEnabled()) && executeMethods.contains(method.getName());
            long t1 = 0;
            if (toLog)
                t1 = System.nanoTime();
            r = method.invoke(targetStatement, args);
            if (r instanceof ResultSet)
                r = ResultSetLoggingHandler.wrapByResultSetProxy((ResultSet) r);
            if (toLog) {
                long t2 = System.nanoTime();
                StringBuffer sb = LogUtils.createLogEntry(method, args == null ? null : args[0].toString(), null, null);
                long time = t2 - t1;

                if (ConfigurationParameters.showTime) {
                    BigDecimal t = (new BigDecimal(t2)).subtract(new BigDecimal(t1)).divide(new BigDecimal(1000000000));
                    sb.append(" ").append(t).append(" s.");
                }

                StatementLogger.info(sb.toString());

                if (time >= ConfigurationParameters.slowQueryThreshold) {
                    SlowQueryLogger.info(sb.toString());
                }

            }
        } catch (Throwable t) {
            LogUtils.handleException(t, StatementLogger.getLogger(), LogUtils.createLogEntry(method, args[0].toString(), null, null));
        }
        return r;
    }

}
