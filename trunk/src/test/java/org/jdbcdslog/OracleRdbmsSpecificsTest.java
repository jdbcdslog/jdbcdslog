package org.jdbcdslog;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ShunLi
 */
public class OracleRdbmsSpecificsTest {
    private RdbmsSpecifics oracleRdbmsSpecifics;
    private Calendar cal;

    @Before
    public void init() {
        oracleRdbmsSpecifics = new OracleRdbmsSpecifics();
        cal = new GregorianCalendar(2011, 0, 1, 23, 59, 59);
    }

    @Test
    public void testFormatStringParameter() {
        String string = "IN\\ST\\$S4R0'3&0'11";
        assertEquals("'IN\\\\ST\\\\\\$S4R0''3'||chr(38)||'0''11'", oracleRdbmsSpecifics.formatParameter(string));
    }

    @Test
    public void testFormatDateParameter() {
        Date date = cal.getTime();
        assertEquals("to_date('2011-01-01', 'yyyy-MM-dd')", oracleRdbmsSpecifics.formatParameter(date));
    }

    @Test
    public void testFormatTimeStampParameter() {
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        assertEquals("to_timestamp('2011-01-01 23:59:59.000', 'yyyy-MM-dd hh24:mi:ss.ff3')", oracleRdbmsSpecifics.formatParameter(timestamp));
    }

    @Test
    public void testFormatBooleanParameter() {
        Boolean bool = Boolean.TRUE;
        assertEquals("Y", oracleRdbmsSpecifics.formatParameter(bool));
    }

    @Test
    public void testFormatObjectParameter() {
        Long value = 80L;
        assertEquals("80", oracleRdbmsSpecifics.formatParameter(value));
    }
}
