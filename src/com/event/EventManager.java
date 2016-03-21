package com.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import com.core.Core;

/**
 * @author Albert Beaupre
 */
public class EventManager {

	private HashMap<EventPriority, LinkedList<HandlerExecutor>> listeners;

	/**
	 * Constructs an empty {@code EventManager}.
	 */
	public EventManager() {
		listeners = new HashMap<>(EventPriority.values().length);
	}

	/**
	 * Returns true if the given base class is a subclass of the given
	 * superclass.
	 * 
	 * @param base
	 *            The base class
	 * @param superClazz
	 *            The class which might be a super class
	 * @return true if the given base class is a subclass of the given
	 *         superclass.
	 */
	private static boolean isSuperClass(Class<?> base, Class<?> superClazz) {
		while (base != null) {
			if (base == superClazz)
				return true;
			base = base.getSuperclass();
		}
		return false;
	}

	/**
	 * Registers the given action listener with the given priority. A listener
	 * may not be registered twice.
	 * 
	 * @param listener
	 *            The listener object
	 * @param priority
	 *            The priority to register it as. Parsing null will set priority
	 *            to ActionPriority.NORMAL
	 */
	public synchronized void register(EventListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listeners may not be null.");
		}

		LinkedList<HandlerExecutor> handlers = getHandlers(listener);
		if (handlers.isEmpty()) {
			if (Core.isDebugEnabled()) {
				System.out.println("<=== WARNING ===>");
				System.out.println("Event listener: " + listener.getClass().getCanonicalName());
				System.out.println("Has no methods which will be registered for events. Are there any defined?");
				System.out.println("Please check all listeners have the @EventHandler annotation.");
				System.out.println("<=== WARNING ===>");
			}
		}

		for (HandlerExecutor h : handlers) {
			LinkedList<HandlerExecutor> list = listeners.get(h.getPriority());
			if (list == null) {
				list = new LinkedList<HandlerExecutor>();
				listeners.put(h.getPriority(), list);
			}
			list.add(h);
		}
	}

	/**
	 * Fetches all of the <br/>
	 * <b>@ActionHandler</b><br/>
	 * methods from the given listener object
	 * 
	 * @param listener
	 *            The listener to fetch handlers from
	 * @return A linked list (Never null, possibly empty) of handlers.
	 */
	private LinkedList<HandlerExecutor> getHandlers(EventListener listener) {
		LinkedList<HandlerExecutor> actions = new LinkedList<HandlerExecutor>();
		for (Method m : listener.getClass().getMethods()) {
			if (!isHandler(m))
				continue;

			HandlerExecutor exec = new HandlerExecutor(listener, m);
			actions.add(exec);
		}
		return actions;
	}

	/**
	 * Returns true if the given method is an action handler method This also
	 * sets it to accessible if it is not.
	 * 
	 * @param m
	 *            The method.
	 * @return true if the given method is an action handler method
	 */
	private static boolean isHandler(Method m) {
		if (m.getParameterTypes().length != 1)
			return false; // Wrong number of args

		for (Class<?> clazz : m.getParameterTypes()) {
			if (!isSuperClass(clazz, Event.class))
				return false;
			if (!m.isAnnotationPresent(EventHandler.class))
				return false; // There is no @ActionHandler annotation
			if (m.isAccessible() == false)
				m.setAccessible(true);
		}
		return true;
	}

	/**
	 * Unregisters the given listener
	 * 
	 * @param listener
	 *            The listener
	 * @return True if it was removed, false if it could not be found.
	 */
	public synchronized boolean unregister(EventListener listener) {
		LinkedList<HandlerExecutor> handlers = getHandlers(listener);
		for (HandlerExecutor h : handlers) {
			LinkedList<HandlerExecutor> list = listeners.get(h.getPriority());
			if (list != null)
				list.remove(h);
		}
		return false;
	}

	/**
	 * Broadcasts the specified action to all listeners
	 * 
	 * @param event
	 *            The action to broadcast.
	 */
	public synchronized void callEvent(Event event) {
		if (event == null)
			throw new NullPointerException("Event may not be null!");

		for (EventPriority priority : EventPriority.values()) {
			LinkedList<HandlerExecutor> list = listeners.get(priority);
			if (list == null)
				continue;

			for (HandlerExecutor h : list) {
				if (event.isConsumed() && h.isConsumer()) {
					/*
					 * Skip consumers when the event is already consumed or the
					 * event is cancelled
					 */
					continue;
				}

				if (h.isSkipIfCancelled() && event.isCanceled()) {
					/* Skip event handlers which don't want cancelled events */
					continue;
				}
				if (!h.getMethod().getParameterTypes()[0].isInstance(event))
					continue;

				try {
					h.getMethod().invoke(h.getTarget(), event);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}
}