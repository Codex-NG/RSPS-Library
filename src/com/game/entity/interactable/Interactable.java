package com.game.entity.interactable;

public interface Interactable {

	boolean hasOption(String option);

	String getName();

	InteractOption[] getOptions();

}
