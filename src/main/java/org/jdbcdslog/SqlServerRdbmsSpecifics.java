package org.jdbcdslog;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RDBMS specifics for the Sql Server DB.
 *
 * @author ShunLi
 */
public class SqlServerRdbmsSpecifics implements RdbmsSpecifics {
    public SqlServerRdbmsSpecifics() {
        super();
    }

    public String formatParameter(Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            String text = LogUtils.replaceEach(
                    (String) object,
                    new String[] { "\\", "$", "'" },
                    new String[] { "\\\\", "\\$", "''" });

            // handle Matcher's appendReplacement method special characters: \ and $
            // handle sql server sql statment's special characters,like '

            // TODO handle other special characters which i don't know.
            // TODO it has not enought actual test,maybe has some issues,if you use it,please help check it is ok? Thanks.
            // TODO only handle % and _ when use like statment. later processing.

            return "'" + text + "'";
        } else if (object instanceof Timestamp) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "'";
        } else if (object instanceof Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd").format(object) + "'";
        } else if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue() ? "'1'" : "'0'";
        } else {
            return object.toString();
        }
    }
}
