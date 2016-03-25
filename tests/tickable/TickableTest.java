package tickable;

import com.core.tickable.Tickable;

public class TickableTest {
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Tickable() {
				@Override
				public void tick() {
					//System.out.println("TICKING");
					queue(1000);
				}
			}.queue(1000);
		}
	}
}
