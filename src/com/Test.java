package com;

import com.core.LibraryCore;
import com.core.tickable.Tickable;

public class Test {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			final int j = i;
			new Tickable() {
				int count;

				@Override
				public void tick() {
					if (count++ >= 5)
						return;
					System.out.println("COUNTING[" + j + "]: " + count);
					queue(1000);
				}
			}.queue(1000);
		}
		LibraryCore.initialize();
	}
}
