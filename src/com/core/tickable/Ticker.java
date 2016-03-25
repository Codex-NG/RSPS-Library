package com.core.tickable;

import java.util.ArrayDeque;

import com.core.Core;

public class Ticker implements Runnable {

	private class TickableWrapper {

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

	private ArrayDeque<TickableWrapper> tickables;

	public Ticker() {
		this.tickables = new ArrayDeque<>();
	}

	public void queue(Tickable tickable, long period) {
		Core.initialize();
		TickableWrapper wrapper = new TickableWrapper(tickable, period);
		wrapper.startTicking();
		tickables.offer(wrapper);
	}

	@Override
	public void run() {
		TickableWrapper wrapper;
		while ((wrapper = tickables.peek()) != null && wrapper.getDuration() >= wrapper.getPeriod()) {
			wrapper.getTickable().cancel();
			wrapper.getTickable().tick();
			tickables.remove(); // Remove the task, it will not be re-queued.
		}
	}

}
