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
public class SqlServerRdbmsSpecificsTest {
    private RdbmsSpecifics sqlServerRdbmsSpecifics;
    private Calendar cal;

    @Before
    public void init() {
        sqlServerRdbmsSpecifics = new SqlServerRdbmsSpecifics();
        cal = new GregorianCalendar(2011, 0, 1, 23, 59, 59);
    }

    @Test
    public void testFormatStringParameter() {
        String string = "T'e\"s\\t\\$todo";
        assertEquals("'T''e\"s\\\\t\\\\\\$todo'", sqlServerRdbmsSpecifics.formatParameter(string));
    }

    @Test
    public void testFormatDateParameter() {
        Date date = cal.getTime();
        assertEquals("'2011-01-01'", sqlServerRdbmsSpecifics.formatParameter(date));
    }

    @Test
    public void testFormatTimeStampParameter() {
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        assertEquals("'2011-01-01 23:59:59'", sqlServerRdbmsSpecifics.formatParameter(timestamp));
    }


    @Test
    public void testFormatBooleanParameter() {
        Boolean bool = Boolean.TRUE;
        assertEquals("'1'", sqlServerRdbmsSpecifics.formatParameter(bool));
    }

    @Test
    public void testFormatObjectParameter() {
        Long value = 80L;
        assertEquals("80", sqlServerRdbmsSpecifics.formatParameter(value));
    }
}
