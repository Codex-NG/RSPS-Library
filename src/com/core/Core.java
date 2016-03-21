package com.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.core.tickable.Ticker;
import com.event.EventManager;

public class Core {

	public static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(2);
	public static final EventManager EVENTS = new EventManager();
	public static final Ticker TICKER = new Ticker();
	public static final ClassLoader CLASS_LOADER = ClassLoader.getSystemClassLoader();

	private static boolean debugging = true;

	public static void initialize() {
		if (debugging) {
			System.out.println("DEBUGGING: " + (debugging ? "ON" : "OFF"));
		}
		SERVICE.scheduleAtFixedRate(TICKER, 1, 1, TimeUnit.MILLISECONDS);
	}

	public static boolean isDebugEnabled() {
		return debugging;
	}

}
