package action;

import com.game.action.Action;

public class ActionExample extends Action {

	@Override
	public void run() {
		System.out.println("OMFG: " + Math.random());
		stop();
	}

	@Override
	public void onCancel() {
	}

	@Override
	public boolean isCancellable() {
		return true;
	}

}
