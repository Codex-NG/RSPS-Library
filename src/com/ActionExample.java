package com;

import co.paralleluniverse.fibers.SuspendExecution;

import com.core.LibraryCore;
import com.game.action.Action;
import com.game.action.ActionQueue;
import com.game.action.Actor;

public class ActionExample {

	static class MyActor implements Actor {

		private final ActionQueue queue;

		public MyActor() {
			queue = new ActionQueue(this);
		}

		@Override
		public ActionQueue getActions() {
			return queue;
		}

	}

	static class MyActor2 implements Actor {

		private final ActionQueue queue;

		public MyActor2() {
			queue = new ActionQueue(this);
		}

		@Override
		public ActionQueue getActions() {
			return queue;
		}

	}

	static class FirstAction extends Action {

		int treeHealth;

		public FirstAction(Actor owner) {
			super(owner);
			this.treeHealth = (int) (10 * (Math.random()));
		}

		@Override
		protected void run() throws SuspendExecution {
			System.out.println("You begin to chop the tree down (Health=" + treeHealth + ")...");

			Action.wait(600);//stalls the action for a random amount of millseconds
			while (treeHealth-- > 0) {

				Action.wait((int) (Math.random() * 2600)); //stalls the action for a random amount of millseconds//stalls the action for a random amount of millseconds
				System.out.println("You retrieve some willow logs.");
			}
			System.out.println("The tree has successfully been chopped down.");
		}

		@Override
		protected void onCancel() {
			System.out.println("First Action Canceled");
		}

		@Override
		protected boolean isCancellable() {
			return false;
		}

	}

	static class SecondAction extends Action {

		public SecondAction(Actor owner) {
			super(owner);
		}

		@Override
		protected void run() throws SuspendExecution {
			System.out.println("SECOND ACTION QUEUED");
		}

		@Override
		protected void onCancel() {

		}

		@Override
		protected boolean isCancellable() {
			return true;
		}

	}

	public static void main(String[] args) {
		LibraryCore.initialize();
		for (int i = 0; i < 1; i++) {
			MyActor actor = new MyActor();
			actor.queue(new FirstAction(actor));
			actor.queue(new SecondAction(actor));
		}

	}

}
