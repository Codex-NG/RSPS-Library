package com.lib;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Albert Beaupre
 */
public final class ServerLogger extends PrintStream {

	public ServerLogger(OutputStream out) {
		super(out);
	}

	@Override
	public void print(String message) {
		log(new Throwable().getStackTrace(), message);
	}

	@Override
	public void print(boolean message) {
		log(new Throwable().getStackTrace(), message);
	}

	@Override
	public void print(int message) {
		log(new Throwable().getStackTrace(), message);
	}

	public void log(StackTraceElement[] e, Object message) {
		super.print("[" + e[2].getClassName() + ", " + e[2].getMethodName() + "] " + message);
	}
}
