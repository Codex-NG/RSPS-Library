package com.game.skill;

import com.event.Event;

public class SkillLevelUpEvent extends Event {

	private final Skiller skiller;
	private final int oldLevel;
	private final int newLevel;

	public SkillLevelUpEvent(Skiller skiller, int oldLevel, int newLevel) {
		this.skiller = skiller;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
	}

	public Skiller getSkiller() {
		return skiller;
	}

	public int getOldLevel() {
		return oldLevel;
	}

	public int getNewLevel() {
		return newLevel;
	}

}
