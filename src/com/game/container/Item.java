package com.game.container;

public class Item {

	private final int id;
	private int amount;
	private int health;

	public Item(int id, int amount, int health) {
		this.id = id;
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
		this.health = health;
		return this;
	}

	@Override
	public String toString() {
		return String.format("[id=%s, amt=%s, hth=%s]", id, amount, health);
	}

	public boolean isStackable() {
		return false;
	}

}
