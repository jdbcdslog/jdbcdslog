package com.googlecode.usc.jdbcdslog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@SuppressWarnings("rawtypes")
public class LogUtils {

    static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void handleException(Throwable e, Logger l, StringBuffer msg) throws Throwable {
        if (e instanceof InvocationTargetException) {
            Throwable t = ((InvocationTargetException) e).getTargetException();
            if (l.isErrorEnabled())
                l.error(msg + "\nthrows exception: " + t.getClass().getName() + ": " + t.getMessage(), t);
            throw t;
        } else {
            if (l.isErrorEnabled())
                l.error(msg + "\nthrows exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static StringBuffer createLogEntry(Method method, Object sql, String parameters, String namedParameters) {
        String methodName = "createLogEntry() ";
        if (logger.isDebugEnabled())
            logger.debug(methodName);
        StringBuffer s = new StringBuffer(method.getDeclaringClass().getName()).append(".").append(method.getName());
        s.append(" ");
        if (sql != null)
            s.append(sql);
        if (parameters != null) {
            s.append(" parameters: ");
            s.append(parameters);
        }
        if (namedParameters != null) {
            s.append(" named parameters: ");
            s.append(namedParameters);
        }
        return s;
    }

    public static StringBuffer createLogEntry(String sql, TreeMap parameters) {
        StringBuffer s = new StringBuffer(" ");

        if (sql != null) {
            int questionMarkCount = 1;
            Pattern p = Pattern.compile("\\?");
            Matcher m = p.matcher(sql);
            StringBuffer stringBuffer = new StringBuffer();

            while (m.find()) {
                m.appendReplacement(stringBuffer, sqlValueToString(parameters.get(questionMarkCount)));
                questionMarkCount++;
            }
            sql = String.valueOf(m.appendTail(stringBuffer));

            s.append(sql).append(";");
        }

        return s;
    }

    public static String sqlValueToString(Object object) {
        /*
         * if (o == null) return "null"; if (o instanceof String) return "'" + o.toString() + "'"; else if (o instanceof Reader && ConfigurationParameters.logText) { return readFromReader((Reader) o); } else return o.toString();
         */
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            return "'" + ((String) object).replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$") + "'"; // Oracle sql ' is special characters
        } else if (object instanceof Timestamp) {
            return "to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(object) + "', 'yyyy-MM-dd hh24:mi:ss.ff3')";
        } else if (object instanceof Date) {
            return "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(object) + "', 'yyyy-MM-dd')";
        } else if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue() ? "Y" : "N";
        } else {
            return object.toString();
        }
    }

    public static String getStackTrace() {
        if (!ConfigurationParameters.printStackTrace)
            return "";
        StackTraceElement stackTraces[] = new Throwable().getStackTrace();
        StringBuffer sb = new StringBuffer(" at ");
        sb.append(stackTraces[4]);
        return sb.toString();
    }
}
