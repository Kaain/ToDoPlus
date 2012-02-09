package de.fhb.mobile.shared.logging;

public class ConsoleLogger implements Logger {

	private Class owner;

	public ConsoleLogger(Class klass) {
		this.owner = klass;
	}

	@Override
	public void debug(String msg, Throwable err) {
		System.out.println("[DEBUG] " + owner.getName() + ": " + msg);
		err.printStackTrace();
	}

	@Override
	public void debug(String msg) {
		System.out.println("[DEBUG] " + owner.getName() + ": " + msg);
	}

	@Override
	public void info(String msg, Throwable err) {
		System.out.println("[INFO] " + owner.getName() + ": " + msg);
		err.printStackTrace();
	}

	@Override
	public void info(String msg) {
		System.out.println("[INFO] " + owner.getName() + ": " + msg);
	}

	@Override
	public void warn(String msg, Throwable err) {
		System.out.println("[WARN] " + owner.getName() + ": " + msg);
		err.printStackTrace();
	}

	@Override
	public void warn(String msg) {
		System.out.println("[WARN] " + owner.getName() + ": " + msg);
	}

	@Override
	public void error(String msg, Throwable err) {
		System.err.println("[ERROR] " + owner.getName() + ": " + msg);
		err.printStackTrace();
	}

	@Override
	public void error(String msg) {
		System.err.println("[ERROR] " + owner.getName() + ": " + msg);
	}

	@Override
	public void setLoggingKlass(Class klass) {
		this.owner = klass;
	}

}
