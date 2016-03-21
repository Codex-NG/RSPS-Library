package com.event;

import com.core.Core;

/**
 * @author Albert Beaupre
 */
public interface EventListener {

	/**
	 * Registers this {@code EventListener} to the {@code EventManager} inside
	 * {@code GameLibrary}.
	 */
	default EventListener register() {
		Core.EVENTS.register(this);
		return this;
	}

	/**
	 * Unregisters this {@code EventListener} from the {@code EventManager}
	 * inside {@code GameLibrary}.
	 */
	default EventListener unregister() {
		Core.EVENTS.unregister(this);
		return this;
	}

}
