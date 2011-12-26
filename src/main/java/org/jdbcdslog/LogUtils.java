package org.jdbcdslog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class LogUtils {

    static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    private final static String NAMED_PARAMETERS_PREFIX = ":";

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

    public static StringBuffer createLogEntry(Method method, String sql, TreeMap parameters, TreeMap namedParameters) {
        StringBuffer s = new StringBuffer();
        if (method != null) {
            s.append(method.getDeclaringClass().getName()).append(".").append(method.getName()).append(": ");
        }

        if (parameters != null && !parameters.isEmpty()) {
            s.append(createLogEntry(sql, parameters));
        } else {
            s.append(createLogEntryForNamedParameters(sql, namedParameters));
        }

        return s;
    }

    public static StringBuffer createLogEntry(String sql, TreeMap parameters) {
        StringBuffer s = new StringBuffer();

        if (sql != null) {
            int questionMarkCount = 1;
            Pattern p = Pattern.compile("\\?");
            Matcher m = p.matcher(sql);
            StringBuffer stringBuffer = new StringBuffer();

            while (m.find()) {
                m.appendReplacement(stringBuffer, ConfigurationParameters.rdbmsSpecifics.formatParameter(parameters.get(questionMarkCount)));
                questionMarkCount++;
            }
            sql = String.valueOf(m.appendTail(stringBuffer));

            s.append(sql).append(";");
        }

        return s;
    }

    @SuppressWarnings("unchecked")
    public static StringBuffer createLogEntryForNamedParameters(String sql, TreeMap namedParameters) {
        StringBuffer s = new StringBuffer();

        if (sql != null) {
            if (namedParameters != null && !namedParameters.isEmpty()) {
                for (String key : (Set<String>) namedParameters.keySet()) {
                    sql = sql.replaceAll(NAMED_PARAMETERS_PREFIX + key, ConfigurationParameters.rdbmsSpecifics.formatParameter(namedParameters.get(key)));
                }
            }
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

    // Refer apache common lang StringUtils.
    public static String replaceEach(String text, String[] searchList, String[] replacementList) {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if (text == null || text.length() == 0 || searchList == null ||
                searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }

        int searchLength = searchList.length;
        int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: "
                    + searchLength
                    + " vs "
                    + replacementLength);
        }

        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                    searchList[i].length() == 0 || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        StringBuilder buf = new StringBuilder(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                        searchList[i].length() == 0 || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        String result = buf.toString();

        return result;
    }
}
