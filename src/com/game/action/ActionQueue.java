package com.game.action;

import java.util.Iterator;
import java.util.LinkedList;

import com.core.tickable.Tickable;

/**
 * @author Albert Beaupre
 */
public class ActionQueue extends Tickable {

	private final LinkedList<Action> actions;
	private long delay;

	/**
	 * Constructs a new {@code ActionQueue}.
	 */
	public ActionQueue() {
		this.setDelay(600);
		this.actions = new LinkedList<>();
	}

	/**
	 * Queues the specified {@code action} for execution.
	 * 
	 * @param action
	 *            the action to be queued.
	 */
	public void queue(Action action) {
		if (actions.offer(action))
			queue();
	}

	/**
	 * This method only runs when there are actions queued.
	 * 
	 * <p>
	 * This method executes any actions in its queue in the order that each
	 * action was queued in. As an {@code Action} that was executed stops
	 * running, it is removed from the current queue and the next action is
	 * executed. If an {@link Action#isCancellable()} returns false, then the
	 * next action in the queue wont execute until the current uncancellable
	 * action is completely stopped.
	 * </p>
	 * 
	 */
	public final void tick() {
		if (actions.isEmpty())
			return;

		Action action = actions.peek();
		if (action != null) {
			action.run();
			if (!action.isRunning()) {
				action.onCancel();
				actions.remove();
				queue();
				return;
			}
			queue(this.delay);
		}
	}

	/**
	 * Clears all cancellable actions within this {@code ActionQueue}.
	 */
	public final void clear() {
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext()) {
			Action action = iterator.next();
			if (action.isCancellable()) {
				cancel(action);
			}
		}
	}

	/**
	 * Cancels the specified {@code action}.
	 * 
	 * @param action
	 *            the action to be cancelled
	 */
	public final void cancel(Action action) {
		if (actions.remove(action)) {
			action.onCancel();
		}
	}

	/**
	 * Returns the delay at which this {@code ActionQueue} ticks at.
	 * 
	 * @return the delay.
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * 
	 * @param delay
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

}
