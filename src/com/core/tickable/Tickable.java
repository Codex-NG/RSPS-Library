package com.core.tickable;

import com.core.Core;

/**
 * A Tickable class is used for timing. Tickables can be queued between periods
 * continually or just once. The {@code Ticker} class queues tickables and
 * executes them based on their queue sequence, which isn't noticeable for a
 * large amount of queues.
 * 
 * @author Albert Beaupre
 */
public abstract class Tickable {

	private boolean queued;

	/**
	 * Ticks this {@code Tickable} after the {@link #queue(long)} method has
	 * been called and the specified period has passed.
	 */
	public abstract void tick();

	/**
	 * Queues this {@code Tickable} for execution after the specified
	 * {@code period} has passed.
	 * 
	 * @param period
	 *            the period before this tickable will queued for execution.
	 */
	public void queue(long period) {
		if (queued)
			return;
		Core.TICKER.queue(this, period);
		queued = true;
	}

	/**
	 * Queues this {@code Tickable} instantly. This method is effectively
	 * equivalent to calling:
	 * 
	 * <pre>
	 * queue(0);
	 * </pre>
	 */
	public void queue() {
		queue(0);
	}

	public boolean isQueued() {
		return queued;
	}

	public void cancel() {
		queued = false;
	}
}
