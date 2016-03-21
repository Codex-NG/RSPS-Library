import com.core.Core;
import com.core.tickable.Tickable;

public class TickableTest extends Tickable {

	@Override
	public void tick() {
		System.out.println("TICKING");
		queue(1000);
	}

	public static void main(String[] args) {
		Core.initialize();
		new TickableTest().queue(1000);
	}
}
