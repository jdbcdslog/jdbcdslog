package org.jdbcdslog;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;

public class LogUtilsTest {
    @Test
    public void testReplaceEach() {
        assertEquals("dcabe", LogUtils.replaceEach("abcde", new String[] { "ab", "d" }, new String[] { "d", "ab" }));
    }

    @Test
    public void testOracleCreateLogEntry() {
        String sql = "select * from mc_instr where instr_cde = ? and instcl_id = ? and expr_date = ? and last_upd_time = ?";
        TreeMap<Integer, Object> parameters = new TreeMap<Integer, Object>();
        parameters.put(1, "IN\\ST\\$S4R0'3&0'11\n1\t2");
        parameters.put(2, 1);

        Calendar cal = new GregorianCalendar(2011, 0, 1, 23, 59, 59);

        parameters.put(3, cal.getTime());
        parameters.put(4, new Timestamp(cal.getTimeInMillis()));

        assertEquals(
                "select * from mc_instr where instr_cde = 'IN\\ST\\$S4R0''3'||chr(38)||'0''11'||chr(10)||'1'||chr(9)||'2' and instcl_id = 1 and expr_date = to_date('2011-01-01', 'yyyy-MM-dd') and last_upd_time = to_timestamp('2011-01-01 23:59:59.000', 'yyyy-MM-dd hh24:mi:ss.ff3');",
                LogUtils.createLogEntry(sql, parameters).toString());

    }

    // TODO: i want to mock the static variable(ConfigurationParameters.rdbmsSpecifics) in unit test,but failure,if you have good advices or ideas,please share with me.Thanks.
    // I tried to use powermock,but found it is work on mock static method(private or public), i could't find some tips to mock static var. Sorry.
    // you need remove @Ignore annotation and modify jdbcdslog.properties file under src/test/resources folder ,
    // change the "jdbcdslog.driverName" property to you need,you may choose "oracle","mysql" ,"sqlserver" or empty (Case-insensitive and does not need the double quotes)

    @Ignore
    @Test
    public void testMySqlCreateLogEntry() {
        String sql = "INSERT INTO test VALUES (?, ?, ?, ?, ?, ?)";

        TreeMap<Integer, Object> parameters = new TreeMap<Integer, Object>();
        parameters.put(1, 1);
        parameters.put(2, "IN\\ST\\$S4\\R\r\n\t0'3&0");

        Calendar cal = new GregorianCalendar(2011, 0, 1, 23, 59, 59);

        parameters.put(3, cal.getTime());
        parameters.put(4, new Timestamp(cal.getTimeInMillis()));
        parameters.put(5, new Time(cal.getTimeInMillis()));

        parameters.put(6, Boolean.TRUE);

        assertEquals(
                "INSERT INTO test VALUES (1, 'IN\\\\ST\\\\$S4\\\\R\\r\\n\\t0\\'3&0', '2011-01-01', '2011-01-01 23:59:59', '23:59:59', '1');",
                LogUtils.createLogEntry(sql, parameters).toString());
        System.out.println(LogUtils.createLogEntry(sql, parameters).toString());

    }

    @Ignore
    @Test
    public void testSqlServerCreateLogEntry() {
        String sql = "INSERT INTO test VALUES (?, ?, ?, ?, ?)";

        TreeMap<Integer, Object> parameters = new TreeMap<Integer, Object>();
        parameters.put(1, 1);
        parameters.put(2, "IN\\ST\\$S4\\R0'3&0");

        Calendar cal = new GregorianCalendar(2011, 0, 1, 23, 59, 59);

        parameters.put(3, cal.getTime());
        parameters.put(4, new Timestamp(cal.getTimeInMillis()));

        parameters.put(5, Boolean.TRUE);

        assertEquals(
                "INSERT INTO test VALUES (1, 'IN\\ST\\$S4\\R0''3&0', '2011-01-01', '2011-01-01 23:59:59', '1');",
                LogUtils.createLogEntry(sql, parameters).toString());
        System.out.println(LogUtils.createLogEntry(sql, parameters).toString());

    }

}
