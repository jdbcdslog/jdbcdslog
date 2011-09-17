package org.jdbcdslog;

/**
 * Encapsulate sql formatting details about a particular relational database management system so that accurate, useable SQL can be composed for that RDMBS.
 *
 * @author ShunLi
 */
public interface RdbmsSpecifics {

    String formatParameter(Object object);

}
