package com.core.tickable;

import com.core.LibraryCore;

public abstract class Tickable {

	public boolean queued;

	public abstract void tick();

	public void queue(long period) {
		LibraryCore.TICKER.queue(this, period);
	}

	public void queue() {
		queue(0);
	}
}
