package org.jdbcdslog;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RDBMS specifics for the MySql DB.
 *
 * @author ShunLi
 */
public class MySqlRdbmsSpecifics implements RdbmsSpecifics {
    public MySqlRdbmsSpecifics() {
        super();
    }

    public String formatParameterObject(Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            // handle replaceAll's special characters: \ \$
            // TODO handle mysql sql's special characters,like...
            // handle Oracle sql's special characters,like '
            String text = ((String) object);

            // String value = ((String) object).replaceAll("\\\\", ).replaceAll("\\$");
            // value = value.replaceAll("'")
            // .replaceAll("\"")
            // TODO only handle % _ when use like statment. later processing. and must check Oracle,is same specifily.
            // .replaceAll("%", "\\\\\\%")
            // .replaceAll("_", "\\\\\\_")
            ;

            return "'" + LogUtils.replaceEach(text, new String[] { "\\", "\\$", "'", "\"", "\\" }, new String[] {"\\\\\\", "\\\\\\$", "\\'", "\\\"", "\\\\" }) + "'";
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
