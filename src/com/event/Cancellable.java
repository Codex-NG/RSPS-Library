package com.event;

public interface Cancellable {

	boolean isCancelled();

	void setCancelled(boolean cancelled);

}
