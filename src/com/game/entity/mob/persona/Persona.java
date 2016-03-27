package com.game.entity.mob.persona;

import com.game.action.ActionQueue;
import com.game.entity.interactable.InteractOption;
import com.game.entity.interactable.Interactable;
import com.game.entity.mob.Mob;
import com.game.skill.SkillManager;
import com.game.skill.Skiller;

public class Persona extends Mob implements Interactable, Skiller {

	private final ActionQueue actions;
	private final SkillManager skills;

	public Persona() {
		this.skills = new SkillManager(this);
		this.actions = new ActionQueue();
	}

	@Override
	public ActionQueue getActions() {
		return actions;
	}

	@Override
	public boolean hasOption(String option) {
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public InteractOption[] getOptions() {
		return null;
	}

	@Override
	public SkillManager getSkills() {
		return skills;
	}

}
