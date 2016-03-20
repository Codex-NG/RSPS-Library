package com.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;

import com.core.tickable.Ticker;
import com.event.EventManager;
import com.module.ModuleLoader;

public class LibraryCore {

	public static final ExecutorService CACHED_POOL = Executors.newCachedThreadPool();
	public static final FiberScheduler FIBER_SCHEDULER = new FiberExecutorScheduler("ServerThread-FiberExec", CACHED_POOL);
	public static final EventManager EVENTS = new EventManager();
	public static final Ticker TICKER = new Ticker();
	public static boolean DEBUGGING = true;
	public static final ClassLoader CLASS_LOADER = ClassLoader.getSystemClassLoader();
	
	public static ModuleLoader MODULE_LOADER;

	private static boolean initialized = false;

	public static void initialize() {
		initialized = true;
		submitTask(TICKER);
		System.out.println("Debugging Mode: " + (DEBUGGING ? "ON" : "OFF"));
		if (MODULE_LOADER == null) {
			System.err.println("A default Module Loader has not been set.");
		} else {
			MODULE_LOADER.load();
		}
	}

	public static void submitTask(Runnable task) {
		if (!initialized)
			throw new IllegalStateException("The library core has not been initialized.");
		CACHED_POOL.submit(task);
	}

}
