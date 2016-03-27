package com.game.entity.interactable;

public abstract class InteractOption {

	private final String text;

	public InteractOption(String text) {
		this.text = text;
	}

	public abstract void run(Object interactor, Interactable target);

	public String getText() {
		return text;
	}

}
