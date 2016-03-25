package action;

public class ActionMain {

	public static void main(String[] args) {
		ActorExample actor = new ActorExample();
		actor.queue(new ActionExample());
		actor.queue(new ActionExample());
		actor.queue(new ActionExample2(3));
		actor.queue(new ActionExample2(2));
	}
	
}
