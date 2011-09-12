package org.jdbcdslog;


/**
 * RDBMS specifics for the Oracle DB.
 *
 * @author ShunLi
 */
public class OracleRdbmsSpecifics extends RdbmsSpecifics {
    public OracleRdbmsSpecifics() {
        super();
    }

    public String formatParameterObject(Object object) {
        return super.formatParameterObject(object);
    }
}
