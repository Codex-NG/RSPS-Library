package event;

import com.event.EventHandler;
import com.event.EventListener;

public class EventListenerExample implements EventListener {

	@EventHandler()
	public void doWhatever(EventExample event) {
		System.out.println(event.getRandomString());
	}

}
