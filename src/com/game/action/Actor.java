package com.game.action;

/**
 * @author Albert Beaupre
 */
public interface Actor {

	/**
	 * Returns the {@code ActionQueue} of this {@code Actor}.
	 * 
	 * @return the action queue
	 */
	ActionQueue getActions();

	default void queue(Action action) {
		getActions().queue(action);
	}

}
