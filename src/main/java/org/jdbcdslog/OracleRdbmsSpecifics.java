package org.jdbcdslog;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RDBMS specifics for the Oracle DB.
 *
 * @author ShunLi
 */
public class OracleRdbmsSpecifics implements RdbmsSpecifics {
    public OracleRdbmsSpecifics() {
        super();
    }

    public String formatParameter(Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            String text = LogUtils.replaceEach(
                    (String) object,
                    new String[] { "\\", "$", "'", "&", "\r", "\n", "\t" },
                    new String[] { "\\\\", "\\$", "''", "'||chr(38)||'", "", "'||chr(10)||'", "'||chr(9)||'" });

            // handle Matcher's appendReplacement method special characters: \ and $
            // handle Oracle sql statment's special characters,like ' and & and \r, \n,\t

            // TODO only handle % and _ when use like statment. later processing.

            return "'" + text + "'";
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
}
