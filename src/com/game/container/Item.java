package com.game.container;

import com.lib.config.ConfigSection;
import com.lib.config.YMLSerializable;

public class Item implements YMLSerializable {

	private short id;
	private int amount;
	private byte health;

	public Item(int id, int amount, int health) {
		this.id = (short) id;
		this.amount(amount);
		this.health(health);
	}

	public Item(int id, int amount) {
		this(id, amount, 0);
	}

	public Item(int id) {
		this(id, 1);
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public Item amount(int amount) {
		this.amount = amount;
		return this;
	}

	public int getHealth() {
		return health;
	}

	public Item health(int health) {
		this.health = (byte) health;
		return this;
	}

	public boolean isStackable() {
		return false;
	}

	@Override
	public String toString() {
		return String.format("[id=%s, amt=%s, health=%s]", id, amount, health);
	}

	@Override
	public ConfigSection serialize() {
		ConfigSection config = new ConfigSection();
		return config.set("id", id).set("amount", amount).set("health", health);
	}

	@Override
	public YMLSerializable deserialize(ConfigSection map) {
		this.id = map.getShort("id");
		this.amount = map.getInt("amount");
		this.health = map.getByte("health");
		return this;
	}

}
