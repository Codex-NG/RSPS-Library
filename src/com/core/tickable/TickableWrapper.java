package com.core.tickable;

public class TickableWrapper {

	private final long period;
	private final Tickable tickable;

	private long startedTicking;

	public TickableWrapper(Tickable tickable, long period) {
		this.tickable = tickable;
		this.period = period;
	}

	public void startTicking() {
		this.startedTicking = System.currentTimeMillis();
	}

	public long getPeriod() {
		return period;
	}

	public Tickable getTickable() {
		return tickable;
	}

	public long getDuration() {
		return System.currentTimeMillis() - startedTicking;
	}

}
