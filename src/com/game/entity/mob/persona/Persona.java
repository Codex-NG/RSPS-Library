package com.game.entity.mob.persona;

import com.game.action.ActionQueue;
import com.game.entity.mob.Mob;

public class Persona extends Mob {

	private final ActionQueue actions;

	public Persona() {
		this.actions = new ActionQueue();
	}

	@Override
	public ActionQueue getActions() {
		return actions;
	}

}
