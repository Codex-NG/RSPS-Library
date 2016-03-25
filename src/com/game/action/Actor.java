package com.game.action;

public interface Actor {

	ActionQueue getActions();

	public default void queue(Action action) {
		getActions().queue(action);
	}

}
