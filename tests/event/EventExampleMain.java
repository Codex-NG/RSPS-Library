package event;

public class EventExampleMain {

	public static void main(String[] args) {
		EventListenerExample listener = new EventListenerExample();
		EventExample event = new EventExample("Testing: " + Math.random());
		
		listener.register(); //Registers the event listener to listen for events

		/**
		 * Calls the EventExample event so that any listeners that hold methods
		 * containing the EventExample parent will be called.
		 */
		event.call();
	}

}
