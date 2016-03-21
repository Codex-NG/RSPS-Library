package tickable;

import com.core.Core;
import com.core.tickable.Tickable;

public class TickableTest {
	public static void main(String[] args) {
		Core.initialize(); //This must be initialized so that 
		new Tickable() {
			@Override
			public void tick() {
				System.out.println("TICKING");
				queue(1000);
			}
		}.queue(1000);
	}
}
