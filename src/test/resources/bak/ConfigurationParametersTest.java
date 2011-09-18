package org.jdbcdslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.expectPrivate;
import static org.powermock.api.easymock.PowerMock.mockStaticPartial;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.mockito.Mockito.*;

/**
 *
 * @author ShunLi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationParameters.class)
public class ConfigurationParametersTest {
    @Test
    public void testGetSProperties() {
        assertEquals("slowQueryThreshold", 1000L, ConfigurationParameters.slowQueryThreshold);
        assertTrue("logText", ConfigurationParameters.logText);
        assertTrue("showTime", ConfigurationParameters.showTime);
        assertTrue("printStackTrace", ConfigurationParameters.printStackTrace);
        assertTrue("rdbmsSpecifics", ConfigurationParameters.rdbmsSpecifics instanceof OracleRdbmsSpecifics);
    }

    @Test
    public void testMockPrivateStatic() throws Exception {
        mockStaticPartial(ConfigurationParameters.class, "initRdbmsSpecifics");

        RdbmsSpecifics expected = new MySqlRdbmsSpecifics();
//        when(ConfigurationParameters.rdbmsSpecifics).thenReturn(expected);
        expectPrivate(ConfigurationParameters.class, "initRdbmsSpecifics", new Object[] {}).andReturn(expected);

        replay(ConfigurationParameters.class);

        RdbmsSpecifics actual = Whitebox.invokeMethod(ConfigurationParameters.class, "initRdbmsSpecifics", new Object[] {});

        verify(ConfigurationParameters.class);
        assertEquals(expected, actual);

        System.out.println(ConfigurationParameters.rdbmsSpecifics);
    }

    @Test
    public void test() throws Exception{
////        PowerMockito.mockStatic(ConfigurationParameters.class);
//        RdbmsSpecifics expected = new MySqlRdbmsSpecifics();
//        PowerMockito.when(ConfigurationParameters.rdbmsSpecifics).thenReturn(expected);
//
////        replay(ConfigurationParameters.class);
//
//        System.out.println(ConfigurationParameters.rdbmsSpecifics);
//        verify(ConfigurationParameters.class);

        RdbmsSpecifics c1 = mock(MySqlRdbmsSpecifics.class);

        PowerMockito.whenNew(RdbmsSpecifics.class) ;// .thenReturn(c1);//.withArguments(c1).thenReturn(c2);
        reset(c1);

        System.out.println(ConfigurationParameters.rdbmsSpecifics);
        System.out.println(c1);

//        PowerMockito.mockStatic(RdbmsSpecifics.class);
//        when(ConfigurationParameters.rdbmsSpecifics).thenReturn(c1);
    }

}
