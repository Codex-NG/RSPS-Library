import com.event.Event;
import com.event.EventHandler;
import com.event.EventListener;

public class EventExample extends Event implements EventListener {

	private final String name;

	public EventExample(String name) {
		this.name = name;
	}

	@EventHandler()
	public void myMethod(EventExample event) {
		System.out.println(event.name);
	}

	public static void main(String[] args) {
		EventExample event = new EventExample("Testing: " + Math.random());
		event.register();
		event.call();
	}

}
