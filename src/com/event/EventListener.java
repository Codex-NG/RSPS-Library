package com.event;

import com.core.LibraryCore;

/**
 * @author Albert Beaupre
 */
public interface EventListener {

	/**
	 * Registers this {@code EventListener} to the {@code EventManager} inside
	 * {@code GameLibrary}.
	 */
	default void register() {
		LibraryCore.EVENTS.register(this);
	}

	/**
	 * Unregisters this {@code EventListener} from the {@code EventManager}
	 * inside {@code GameLibrary}.
	 */
	default void unregister() {
		LibraryCore.EVENTS.unregister(this);
	}

}
