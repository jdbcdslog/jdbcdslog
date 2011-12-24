package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PreparedStatementLoggingHandler implements InvocationHandler {
    TreeMap parameters = new TreeMap();

    Object target = null;

    String sql = null;

    static List setMethods = Arrays.asList(new String[] { "setAsciiStream", "setBigDecimal", "setBinaryStream", "setBoolean", "setByte", "setBytes", "setCharacterStream", "setDate", "setDouble", "setFloat", "setInt", "setLong", "setObject", "setShort", "setString", "setTime", "setTimestamp", "setURL" });

    static List executeMethods = Arrays.asList(new String[] { "addBatch", "execute", "executeQuery", "executeUpdate" });

    public PreparedStatementLoggingHandler(PreparedStatement ps, String sql) {
        target = ps;
        this.sql = sql;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object r = null;
        try {
            long t1 = 0;
            boolean toLog = (StatementLogger.isInfoEnabled() || SlowQueryLogger.isInfoEnabled()) && executeMethods.contains(method.getName());
            if (toLog)
                t1 = System.currentTimeMillis();
            r = method.invoke(target, args);
            if (setMethods.contains(method.getName()) && args[0] instanceof Integer)
                parameters.put(args[0], args[1]);

            if ("clearParameters".equals(method.getName()))
                parameters = new TreeMap();

            if (toLog) {
                // StringBuffer sb = LogUtils.createLogEntry(method, sql, parametersToString(), null);
                StringBuffer sb = LogUtils.createLogEntry(sql, parameters);

                long t2 = System.currentTimeMillis();
                long time = t2 - t1;
                if (ConfigurationParameters.showTime) {
                    sb.append(" ").append(t2 - t1).append(" ms.");
                }

                StatementLogger.info(sb.toString());

                if (time >= ConfigurationParameters.slowQueryThreshold) {
                    SlowQueryLogger.info(sb.toString());
                }
            }
            if (r instanceof ResultSet)
                r = ResultSetLoggingHandler.wrapByResultSetProxy((ResultSet) r);
        } catch (Throwable t) {
            LogUtils.handleException(t, StatementLogger.getLogger(), LogUtils.createLogEntry(sql, parameters));
        }
        return r;
    }
}
