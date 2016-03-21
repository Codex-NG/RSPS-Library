package com.core.tickable;

import com.core.Core;

public abstract class Tickable {

	public abstract void tick();

	public void queue(long period) {
		Core.TICKER.queue(this, period);
	}

	public void queue() {
		queue(0);
	}
}
