package org.jdbcdslog;

/**
 * RDBMS specifics for the Sql Server DB.
 *
 * @author ShunLi
 */
public class SqlServerRdbmsSpecifics implements RdbmsSpecifics {
    public SqlServerRdbmsSpecifics() {
        super();
    }

    public String formatParameterObject(Object object) {
        return null;
    }
}
