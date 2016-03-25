package action;

import com.game.action.ActionQueue;
import com.game.action.Actor;

public class ActorExample implements Actor {

	private final ActionQueue actions = new ActionQueue();

	@Override
	public ActionQueue getActions() {
		return actions;
	}

}
