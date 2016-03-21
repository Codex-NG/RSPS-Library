package event;

import com.event.Event;

public class EventExample extends Event {

	private final String randomString;

	public EventExample(String randomString) {
		this.randomString = randomString;
	}

	public String getRandomString() {
		return "This is a random string:" + randomString;
	}

}
