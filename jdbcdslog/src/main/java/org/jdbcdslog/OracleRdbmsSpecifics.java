package org.jdbcdslog;


/**
 * RDBMS specifics for the Oracle DB.
 *
 * @author ShunLi
 */
public class OracleRdbmsSpecifics extends RdbmsSpecifics {
    OracleRdbmsSpecifics() {
        super();
    }

    public String formatParameterObject(Object object) {
        return null;
    }
}
