package com.caversia.plugins.votes.bungee.utils;

import java.util.logging.Level;

import com.caversia.plugins.votes.bungee.Main;

import net.md_5.bungee.api.plugin.PluginLogger;

/**
 * Wrapper to the {@link PluginLogger}. Provides global access to logging with lazy evaluation of it's arguments.
 * 
 * @author amartins
 */
//@formatter:off
public class Logger {
	private static java.util.logging.Logger logger = Main.self.getLogger();
	
	/**
	 * Logs a TRACE message.
	 * 
	 * @param message the message to log
	 * @param args the arguments to inject on the message placeholders
	 */
	public static void trace(String message, Object... args) {
		logger.log(Level.FINEST, () -> { return evaluate(message, args); });
	}

	/**
	 * Logs a DEBUG message.
	 * 
	 * @param message the message to log
	 * @param args the arguments to inject on the message placeholders
	 */
	public static void debug(String message, Object... args) {
		logger.log(Level.FINE, () -> { return evaluate(message, args); });
	}

	/**
	 * Logs a INFO message.
	 * 
	 * @param message the message to log
	 * @param args the arguments to inject on the message placeholders
	 */
	public static void info(String message, Object... args) {
		logger.log(Level.INFO, () -> { return evaluate(message, args); });
	}

	/**
	 * Logs a WARN message.
	 * 
	 * @param message the message to log
	 * @param args the arguments to inject on the message placeholders
	 */
	public static void warn(String message, Object... args) {
		logger.log(Level.WARNING, () -> { return evaluate(message, args); });
	}

	/**
	 * Logs a ERROR message.
	 * 
	 * @param message the message to log
	 * @param args the arguments to inject on the message placeholders
	 */
	public static void error(String message, Object... args) {
		logger.log(Level.SEVERE, () -> { return evaluate(message, args); });
	}

	/**
	 * Used to lazy evaluate a log message.
	 * 
	 * @param message the message to evaluate
	 * @param args the arguments to inject on the message placeholders
	 */
	private static String evaluate(String message, Object... args) {
		return args.length == 0 ? message : String.format(message.replace("{}", "%s"), args);
	}
}
//@formatter:on
