package com.core.tickable;

import com.core.LibraryCore;

public abstract class Tickable {

	public abstract void tick();

	public void queue(long period) {
		LibraryCore.TICKER.queue(this, period);
	}
}
