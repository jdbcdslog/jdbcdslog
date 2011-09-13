package org.jdbcdslog;

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
        return null;
    }
}
