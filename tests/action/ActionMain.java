package action;

import tickable.TickableTest;

public class ActionMain {

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			TickableTest.main(args);
		}
		for (int i = 0; i < 1000; i++) {
			ActorExample actor = new ActorExample();
			actor.queue(new ActionExample());
			actor.queue(new ActionExample());
			actor.queue(new ActionExample2(3));
			actor.queue(new ActionExample2(2));
		}
	}

}
