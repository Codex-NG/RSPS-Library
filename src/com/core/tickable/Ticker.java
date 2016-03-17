package com.core.tickable;

import java.util.ArrayDeque;

public class Ticker implements Runnable {

	private ArrayDeque<TickableWrapper> tickables;

	public Ticker() {
		this.tickables = new ArrayDeque<>();
	}

	public void queue(Tickable tickable, long period) {
		TickableWrapper wrapper = new TickableWrapper(tickable, period);
		wrapper.getTickable().queued = true;
		wrapper.startTicking();
		tickables.add(wrapper);
	}

	@Override
	public void run() {
		while (true) {
			TickableWrapper wrapper;
			synchronized (tickables) {
				while ((wrapper = tickables.peek()) != null && wrapper.getDuration() >= wrapper.getPeriod()) {
					wrapper.getTickable().tick();
					wrapper.getTickable().queued = false;
					tickables.remove(); // Remove the task, it will not be re-queued.
				}
			}
		}
	}

}
