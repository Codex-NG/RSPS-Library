package com.game.action;

/**
 * @author Albert Beaupre
 */
public abstract class Action {

	private boolean running;

	/**
	 * Constructs a new {@code Action}.
	 */
	public Action() {
		this.running = true;
	}

	/**
	 * This method is executed after this {@code Action} has been queued and
	 * selected to run in its parenting {@code ActionQueue}.
	 */
	public abstract void run();

	/**
	 * This method is executed after this {@code Action} has been cancelled.
	 */
	public abstract void onCancel();

	/**
	 * Returns true if this {@code Action} can be cancelled and returns false
	 * otherwise.
	 * 
	 * @return true if cancellable; return false otherwise
	 */
	public abstract boolean isCancellable();

	public abstract boolean interrupt();
	
	/**
	 * Stops this {@code Action}.
	 */
	public void stop() {
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
