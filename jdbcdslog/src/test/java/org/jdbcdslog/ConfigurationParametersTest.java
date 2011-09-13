package org.jdbcdslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author ShunLi
 */
public class ConfigurationParametersTest {
    @Test
    public void testGetSProperties() {
        assertEquals("slowQueryThreshold", 1000L, ConfigurationParameters.slowQueryThreshold);
        assertTrue("logText", ConfigurationParameters.logText);
        assertTrue("showTime", ConfigurationParameters.showTime);
        assertTrue("printStackTrace", ConfigurationParameters.printStackTrace);
        assertTrue("rdbmsSpecifics", ConfigurationParameters.rdbmsSpecifics instanceof OracleRdbmsSpecifics);
    }
}
