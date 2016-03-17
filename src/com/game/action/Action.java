package com.game.action;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import com.core.LibraryCore;

/**
 * An abstract class which represents an action that a player may perform, such as combat or
 * chopping an oak tree.
 * 
 * @author netherfoam
 */
public abstract class Action {
	/**
	 * Waits for the given number of ticks. This may only be invoked from a Fiber, otherwise an
	 * exception is thrown. A SuspendExecution is not an exception, but a marker that the method may
	 * cause the current fiber to be suspended. This method may be used without blocking the thread,
	 * but while blocking the current Fiber. This is a neat way of writing events so that they are
	 * cleaner and do not need non-local variables to track states or to be ridden with callbacks.
	 * 
	 * @param ticks
	 *            the number of ticks to wait before returning.
	 * @throws SuspendExecution
	 */
	public static void wait(long delay, TimeUnit unit) throws SuspendExecution {
		if (!Fiber.isCurrentFiber())
			throw new RuntimeException("wait() may only be invoked by a Fiber.");
		long start = System.currentTimeMillis();
		long millis = TimeUnit.MILLISECONDS.convert(delay, unit);
		while (System.currentTimeMillis() - start < millis)
			Fiber.park();
	}
	
	public static void wait(int delay) throws SuspendExecution {
		wait(delay, TimeUnit.MILLISECONDS);
	}


	/** The mob who is performing the action */
	protected final Actor owner;
	/**
	 * The list of actions which are 'paired' to this one. If A is paired with B, then B is paired
	 * with A. If A is cancelled, B should be cancelled and visa versa. If A terminates, B should
	 * not be cancelled.
	 */
	protected LinkedList<Action> paired = new LinkedList<Action>();

	/**
	 * The Fiber which runs this action
	 */
	private Fiber<Void> fiber;

	/**
	 * Constructs a new Action, but does not apply it, for the given mob.
	 * 
	 * @param owner
	 *            the mob to construct the action for
	 * @throws NullPointerException
	 *             if the given mob is null
	 */
	public Action(Actor owner) {
		if (owner == null)
			throw new NullPointerException("The owner of an Action may not be null");
		this.owner = owner;
	}

	/**
	 * Returns the owner of this action, not null
	 * 
	 * @return the owner of this action.
	 */
	public Actor getOwner() {
		return owner;
	}

	/**
	 * Called successively upon a tick when this action is able to be run. This moves the internal
	 * fiber, either creating a new one if it is the first time, or unparking an existing one if it
	 * is a sequential time. If the action has finished, this will raise an exception.
	 */
	protected void tick() {
		if (fiber == null) {
			try {
				fiber = new Fiber<Void>(this.toString(), LibraryCore.FIBER_SCHEDULER) {
					private static final long serialVersionUID = 1842342854418180882L;

					@Override
					public Void run() throws SuspendExecution {
						try {
							Action.this.run();
						} catch (Throwable t) {
							System.out.println("There was an Exception thrown while running an Action. Details:");
							System.out.println("Mob: " + Action.this.getOwner() + ", Action: " + Action.this);
							t.printStackTrace();
						}
						//Notify the action queue this action has ended
						getOwner().getActions().end(Action.this);
						return null;
					}
				};
			} catch (IllegalArgumentException e) {
				if (e.getMessage().contains("instrumented")) {
					System.out.println("It appears that the class " + this + " has not been instrumented.");
					System.out.println("The ClassLoader hierachy is:");
					ClassLoader cl = this.getClass().getClassLoader();
					StringBuilder sb = new StringBuilder(cl.getClass().getCanonicalName());
					while (((cl = cl.getParent())) != null) {
						sb.append(" < " + cl.getClass().getCanonicalName());
					}
					System.out.println(sb.toString());
				}
				throw e;
			}

			//Fiber doesn't get executed right here! It is in the ServerThread list of things to run after this call
			// - And right now, *THIS* is being executed, not the fiber!
			fiber.start();
		} else {
			if (fiber.isTerminated()) {
				return;
				//throw new RuntimeException("Action's Fiber was terminated, but Action was requested to tick() anyway?");
			}
			//Fiber doesn't get executed right here! It is in the ServerThread list of things to run after this call
			// - And right now, *THIS* is being executed, not the fiber!
			fiber.unpark();
		}
	}

	/**
	 * Called when a tick passes and this action is the first action in the queue. If the action has
	 * finished, this method should return true. If the action is not fully complete, it should
	 * return false. If the action is cancellable, then despite returning false, it may not have its
	 * run() method invoked again. When an action is cancelled, whether it started or not, it's
	 * cancel() method will always be invoked.
	 * 
	 * @return true if finished, false if continue to call run() every tick.
	 */
	protected abstract void run() throws SuspendExecution;

	/**
	 * Cancels this action. This is called when it is interrupted or cancelled before it could be
	 * started. If run() returns true, this method will not be called, otherwise it will be called.
	 * This allows cleanup.
	 */
	protected abstract void onCancel();

	/**
	 * Returns true if this action is cancellable (Eg, movement, following, and combat are
	 * cancellable. Eating and being stunned are not). If this method returns false, it can still be
	 * cancelled if the ActionQueue is requested to cancel it specifically. When the ActionQueue has
	 * the clear() method invoked, however, only cancellable Actions will be removed.
	 * 
	 * @return true if this action can be cancelled, false if it should not.
	 */
	protected abstract boolean isCancellable();

	/**
	 * Yields this action's turn to the next action, thus invoking the run() method on the next
	 * action. The next action may yield and so on until one doesn't yield, or the end of the
	 * ActionQueue is reached. Any exceptions thrown are caught by this method. This is a shortcut
	 * to getOwner().getActions().yield(this). This is useful in situations such as combat, where a
	 * Follow is desired until the target is reached, in which case a Follow Action would call
	 * yield(), allowing a Combat action to be executed immediately after.
	 */
	public void yield() {
		getOwner().getActions().yield(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	/**
	 * Pairs this action with another. If one action is cancelled, the other action will also become
	 * cancelled. If one action completes however, the other action will not be cancelled.
	 * 
	 * @param a
	 *            the action to pair it with
	 */
	public void pair(Action a) {
		if (paired.contains(a)) {
			throw new IllegalArgumentException("That action is already paired with this one. Given: " + a);
		}

		paired.add(a);
		if (a.paired.contains(this) == false)
			a.paired.add(this);
	}

	/**
	 * Queues this action, the same as getOwner().getActions().queue(this)
	 */
	public void queue() {
		getOwner().getActions().queue(this);
	}

	/**
	 * Returns true if this action is queued
	 * 
	 * @return true if this action is queued
	 */
	public boolean isQueued() {
		return getOwner().getActions().isQueued(this);
	}

	/**
	 * Cancels this action. This is a convenience method for getOwner().getActions().cancel(this)
	 */
	public void cancel() {
		getOwner().getActions().cancel(this);
	}

	/**
	 * Returns true if this Action completed without being interrupted
	 * 
	 * @return true if this Action completed without being interrupted
	 */
	public boolean isComplete() {
		return fiber.isTerminated();
	}

}