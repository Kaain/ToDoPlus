package de.fhb.mobile.shared.logging;

public class LoggerFactory {

	// the name of the android logger
	private static final String ANDROID_LOGGER = "de.fhb.mobile.shared.android.logging.AndroidLogger";

	// the name of the log4j logger
	private static final String LOG4J_LOGGER = "de.fhb.mobile.middleware.logging.Log4jLogger";

	// we are using the console logger
	private static Logger logger = new ConsoleLogger(LoggerFactory.class);

	public static Logger getLogger(Class klass) {

		// we check whether we can load the android logger available, in which
		// case we will use it. If it is not available, we assume that we are
		// not running on an android device
		try {
			Class loggerklass = Class.forName(ANDROID_LOGGER);
			Logger loggerobj = (Logger) loggerklass.newInstance();

			loggerobj.setLoggingKlass(klass);

			return loggerobj;
		} catch (Throwable t1) {
			logger.info("we could not load the android logger. Got: "
					+ t1
					+ ". supposedly we are not running on an android device. Try out the log4j logger.");
			try {
				Class loggerklass = Class.forName(LOG4J_LOGGER);
				Logger loggerobj = (Logger) loggerklass.newInstance();
				loggerobj.setLoggingKlass(klass);
				
				return loggerobj;
			} catch (Throwable t2) {
				logger.info("we could neither load the android nor the log4j logger. Use console logging.");

				return new ConsoleLogger(klass);
			}
		}

	}

}
