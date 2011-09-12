package org.jdbcdslog;

/**
 * RDBMS specifics for the MySql DB.
 *
 * @author ShunLi
 */
public class MySqlRdbmsSpecifics extends RdbmsSpecifics {
    public MySqlRdbmsSpecifics() {
        super();
    }

    public String formatParameterObject(Object object) {
        return super.formatParameterObject(object);
    }
}
