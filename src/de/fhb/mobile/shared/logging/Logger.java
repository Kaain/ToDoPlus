package de.fhb.mobile.shared.logging;

public interface Logger {

	public void debug(String msg,Throwable err);
	
	public void debug(String msg);

	public void info(String msg,Throwable err);
	
	public void info(String msg);

	public void warn(String msg,Throwable err);
	
	public void warn(String msg);

	public void error(String msg,Throwable err);
	
	public void error(String msg);

	public void setLoggingKlass(Class klass);
	
}
