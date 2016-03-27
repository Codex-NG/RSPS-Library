package com.game.skill;

import java.util.HashMap;

public class SkillManager {

	private final Skiller skiller;
	private HashMap<SkillType, Skill> skills;

	public SkillManager(Skiller skiller) {
		this.skiller = skiller;
		this.skills = new HashMap<>();
		for (SkillType type : SkillType.values()) {
			this.skills.put(type, new Skill(type));
		}
	}

	public Skill getSkill(SkillType type) {
		return skills.get(type);
	}

	public void addExperience(SkillType type, double experience) {
		getSkill(type).addExperience(skiller, experience);
	}

	public int getLevel(SkillType type, boolean modifiers) {
		return getSkill(type).getLevel(modifiers);
	}

	public int getTotalLevel() {
		int totalLevel = 0;
		for (Skill skill : skills.values())
			totalLevel += skill.getLevel(false);
		return totalLevel;
	}

	public int getCombatLevel() {
		int attack = getLevel(SkillType.ATTACK, false);
		int defence = getLevel(SkillType.DEFENCE, false);
		int strength = getLevel(SkillType.STRENGTH, false);
		int hp = getLevel(SkillType.CONSTITUTION, false);
		int prayer = getLevel(SkillType.PRAYER, false);
		int ranged = getLevel(SkillType.RANGE, false);
		int magic = getLevel(SkillType.MAGIC, false);
		int combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel + (getLevel(SkillType.SUMMONING, false) / 8);
	}

	public static int getLevelForExperience(double experience) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl < 120; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= experience) {
				return lvl;
			}
		}
		return 120;
	}

}
