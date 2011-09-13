package org.jdbcdslog;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.junit.Test;

public class LogUtilsTest {

    @Test
    public void testCreateLogEntry() {
        String sql = "select * from mc_instr where instr_cde = ? and instcl_id = ? and expr_date = ? and last_upd_time = ?";
        TreeMap<Integer, Object> parameters = new TreeMap<Integer, Object>();
        parameters.put(1, "IN\\ST\\$S4R0'3&0'11");
        parameters.put(2, 1);

        Calendar cal = new GregorianCalendar(2011, 0, 1, 23, 59, 59);

        parameters.put(3, cal.getTime());
        parameters.put(4, new Timestamp(cal.getTimeInMillis()));

        assertEquals(
                "select * from mc_instr where instr_cde = 'IN\\ST\\$S4R0''3'||chr(38)||'0''11' and instcl_id = 1 and expr_date = to_date('2011-01-01', 'yyyy-MM-dd') and last_upd_time = to_timestamp('2011-01-01 23:59:59.000', 'yyyy-MM-dd hh24:mi:ss.ff3');",
                LogUtils.createLogEntry(sql, parameters).toString());

    }

    @Test
    public void testReplaceEach() {
        assertEquals("dcabe", LogUtils.replaceEach("abcde", new String[] { "ab", "d" }, new String[] { "d", "ab" }));
    }
}
