package com.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.core.tickable.Ticker;
import com.event.EventManager;

/**
 * The {@code Core} class is meant to hold all major referenced pieces of code
 * that will handle most of the library.
 * 
 * @author Albert Beaupre
 */
public class Core {

	public static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(2);
	public static final EventManager EVENTS = new EventManager();
	public static final Ticker TICKER = new Ticker();
	public static final ClassLoader CLASS_LOADER = ClassLoader.getSystemClassLoader();

	private static boolean debugging = true;
	private static boolean initialized;

	/**
	 * This method will initialize the {@code Core}.
	 */
	public static void initialize() {
		if (initialized)
			return;
		System.out.println("Booting Core.");
		if (debugging)
			System.out.println("DEBUGGING: " + (debugging ? "ON" : "OFF"));
		initialized = true;
		SERVICE.scheduleAtFixedRate(TICKER, 1, 1, TimeUnit.MILLISECONDS);
	}

	public static boolean isDebugEnabled() {
		return debugging;
	}
}
