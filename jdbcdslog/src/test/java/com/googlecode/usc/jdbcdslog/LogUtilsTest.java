package com.googlecode.usc.jdbcdslog;

import java.sql.Timestamp;
import java.util.Date;
import java.util.TreeMap;

import org.junit.Test;

public class LogUtilsTest {

    @Test
    public void testCreateLogEntry() {
        String sql = "select * from mc_instr where instr_cde = ? and instcl_id = ? instrumentBusinessDate = ? and last_updat_time = ?";
        TreeMap<Integer, Object> parameters = new TreeMap<Integer, Object>();
        parameters.put(1, "INST\\$S4R0'30'11");
        parameters.put(2, 1);
        parameters.put(3, new Date());
        parameters.put(4, new Timestamp(System.currentTimeMillis()));

        System.out.println(LogUtils.createLogEntry(sql, parameters));

    }

}
