package edu.ncsu.csc.ase.dristi.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import edu.ncsu.csc.ase.dristi.configuration.Config;

/**
 * Wrapper Class for SLF4J Logger Factory
 * 
 * @author Rahul Pandita
 * 
 */
public class MyLoggerFactory {

	private static boolean initialize = false;

	/**
	 * Static method to initialize the default SLF4J logging framework with log
	 * level
	 */
	private static void initialize() {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY,
				Config.DEFAULT_LOG_LEVEL_KEY);
		initialize = true;
	}

	/**
	 * Return a logger named corresponding to the class passed as parameter,
	 * using the statically bound ILoggerFactory instance.
	 * 
	 * @param clazz
	 *            - the returned logger will be named after clazz
	 * @return logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		if (!initialize)
			initialize();
		return LoggerFactory.getLogger(clazz);
	}

	/**
	 * Return a logger named according to the name parameter using the
	 * statically bound ILoggerFactory instance.
	 * 
	 * @param name
	 *            - The name of the logger.
	 * @return logger
	 */
	public static Logger getLogger(String name) {
		if (!initialize)
			initialize();
		return LoggerFactory.getLogger(name);
	}
}
