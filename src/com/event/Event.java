package com.event;

import com.core.LibraryCore;

/**
 * TODO: Instead of creating a (canceled) state, maybe add an EventState, which
 * might have different types of states like: WAITING, CANCELED, CALLED,
 * CONSUMED, etc
 * 
 * 
 * @author Albert Beaupre
 */
public class Event {

	private boolean canceled;
	private boolean consumed;

	/**
	 * Calls this {@code Event} and will listen for any {@code EventListener}
	 * with methods that hold parameters instancing an event.
	 */
	public void call() {
		LibraryCore.EVENTS.callEvent(this);
	}

	/**
	 * Returns true if this {@code Event} has been canceled;
	 * 
	 * @return true if canceled; return false otherwise
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * Sets the canceled state of this {@code Event} to the specified
	 * {@code canceled}.
	 * 
	 * @param canceled
	 *            the canceled flag to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	/**
	 * Returns true if this {@code Event} has been consumed.
	 * 
	 * @return true if consumed; return false otherwise
	 */
	public boolean isConsumed() {
		return consumed;
	}

	/**
	 * Sets the consumed flag of this {@code Event} to the specified
	 * {@code consumed}.
	 * 
	 * @param consumed
	 *            the consumed flag to set
	 */
	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}
}
