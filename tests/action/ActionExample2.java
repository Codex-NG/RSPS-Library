package action;

import com.game.action.Action;

public class ActionExample2 extends Action {

	int count;
	int s;

	public ActionExample2(int count) {
		this.count = count;
	}

	@Override
	public void run() {
		if (s++ >= count) {
			System.out.println("DONE");
			stop();
		} else {

			System.out.println("COUNTING: " + s);
		}
	}

	@Override
	public void onCancel() {

	}

	@Override
	public boolean isCancellable() {
		return false;
	}

}
