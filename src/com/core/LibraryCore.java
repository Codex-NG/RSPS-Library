package com.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.core.tickable.Ticker;
import com.event.EventManager;

public class LibraryCore {

	public static final EventManager EVENTS = new EventManager();
	public static final Ticker TICKER = new Ticker();
	public static boolean DEBUGGING = true;

	private static ExecutorService corePool;

	public static void initialize() {
		corePool = Executors.newCachedThreadPool();
		
		submitTask(TICKER);
	}

	public static void submitTask(Runnable task) {
		if (corePool == null)
			throw new IllegalStateException("The library core has not been initialized.");
		corePool.submit(task);
	}

}
