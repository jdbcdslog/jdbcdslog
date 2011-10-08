package org.jdbcdslog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionLogger {
    private static Logger logger = LoggerFactory.getLogger(ConnectionLogger.class);

    public static void info(String s) {
        logger.info(s + LogUtils.getStackTrace());
    }

    public static void error(String m, Throwable t) {
        logger.error(m, t);
    }

    public static boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public static Logger getLogger() {
        return logger;
    }
}
