package com.game.skill;

import com.lib.attribute.AttributedNumber;
import com.lib.config.ConfigSection;
import com.lib.config.YMLSerializable;

/**
 * @author Albert Beaupre
 */
public class Skill implements YMLSerializable {

	private SkillType type;
	private double experience;

	private AttributedNumber level;

	public Skill(SkillType type) {
		this.type = type;
		this.level = new AttributedNumber(1);
	}

	public Skill addExperience(Skiller skiller, double exp) {
		int oldLevel = getLevel(false);
		this.experience += exp;
		this.level.setDefaultNumber(SkillManager.getLevelForExperience(this.experience));
		int newLevel = getLevel(false);
		if (newLevel > oldLevel) {
			SkillLevelUpEvent event = new SkillLevelUpEvent(skiller, oldLevel, newLevel);
			event.call();
		}
		return this;
	}

	public double getExperience() {
		return experience;
	}

	public int getLevel(boolean modifier) {
		return modifier ? (int) level.getValue() : (int) level.getDefaultNumber();
	}

	@Override
	public ConfigSection serialize() {
		return new ConfigSection().set("type", type).set("experience", experience).set("level", level.serialize());
	}

	@Override
	public Skill deserialize(ConfigSection map) {
		this.type = map.get("type", SkillType.class);
		this.experience = map.getDouble("experience");
		this.level = new AttributedNumber().deserialize(map.getSection("level"));
		return this;
	}
}
