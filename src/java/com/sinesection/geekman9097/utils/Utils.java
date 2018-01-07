package com.sinesection.geekman9097.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Utils {
	
	private static final String DEFAULT_TITLE = "Uninitialized Logger";
	
	private static Logger logger;
	
	public static void createLogger(String title) {
		logger = LogManager.getFormatterLogger(title);
	}
	
	public static Logger getLogger() {
		if(logger == null) logger = LogManager.getFormatterLogger(DEFAULT_TITLE);
		return logger;
	}
}
