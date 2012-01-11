package org.jdbcdslog;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
	
	static Logger logger = LoggerFactory.getLogger(LogUtils.class);
	
	public static void handleException(Throwable e, Logger l, StringBuffer msg) 
		throws Throwable {
		if(e instanceof InvocationTargetException) {
			Throwable t = ((InvocationTargetException)e).getTargetException();
			if(l.isErrorEnabled()) 
				l.error(msg + " throws exception: " + t.getClass().getName() + ": "
					+ t.getMessage(), t);
			throw t;
		} else {
			if(l.isErrorEnabled())
				l.error(msg + " throws exception: " + e.getClass().getName() + ": "
						+ e.getMessage(), e);
			throw e;
		}
	}
	
	public static StringBuffer createLogEntry(Method method, Object sql, String parameters, String namedParameters) {
		String methodName = "createLogEntry() ";
		if(logger.isDebugEnabled()) logger.debug(methodName);
		StringBuffer s = new StringBuffer(method.getDeclaringClass().getName())
		.append(".").append(method.getName());
		s.append(" ");
		if(sql != null)
			s.append(sql);
		if(parameters != null) {
			s.append(" parameters: ");
			s.append(parameters);
		}
		if(namedParameters != null) {
			s.append(" named parameters: ");
			s.append(namedParameters);
		}
		return s;
	}

	public static String sqlValueToString(Object o) {
		if(o == null)
			return "null";
		if(o instanceof String)
			return "'" + o.toString() + "'";
		else if(o instanceof Reader && ConfigurationParameters.logText) {
			return readFromReader((Reader)o);
		}
		else
			return o.toString();
	}

	private static String readFromReader(Reader sr) {
		String methodName = "readFromReader() ";
		StringBuffer sb = new StringBuffer();
		int read = 0;
		char buf[] = new char[1024];
		try {
			sr.reset();
			do {
				read = sr.read(buf, 0, 1024);
				if(logger.isDebugEnabled()) logger.debug(methodName + "read = " + read);
				if(read != -1)
					sb.append(buf, 0, read);
			} while(read != -1);
			sr.reset();
		} catch(IOException e) {
			logger.error(e.getMessage(), e);
		}
		sb.insert(0, "'");
		sb.append("'");
		return sb.toString();
	}
	
	public static String getStackTrace() {
		if(!ConfigurationParameters.printStackTrace)
			return "";
		StackTraceElement stackTraces[] = new Throwable().getStackTrace();
		StringBuffer sb = new StringBuffer(" at ");
		sb.append(stackTraces[4]);
		return sb.toString();
	}
}
