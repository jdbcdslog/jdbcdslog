package org.jdbcdslog;

import java.lang.reflect.InvocationTargetException;
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

/*    public static StringBuffer createLogEntry(Method method, Object sql, String parameters, String namedParameters) {
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
    }*/

    public static StringBuffer createLogEntry(String sql, TreeMap parameters) {
        StringBuffer s = new StringBuffer();

        if (sql != null) {
            int questionMarkCount = 1;
            Pattern p = Pattern.compile("\\?");
            Matcher m = p.matcher(sql);
            StringBuffer stringBuffer = new StringBuffer();

            while (m.find()) {
                m.appendReplacement(stringBuffer, ConfigurationParameters.rdbmsSpecifics.formatParameterObject(parameters.get(questionMarkCount)));
                questionMarkCount++;
            }
            sql = String.valueOf(m.appendTail(stringBuffer));

            s.append(sql).append(";");
        }

        return s;
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
