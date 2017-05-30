package com.github.TheDwoon.robots.server.broadcaster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public abstract class Broadcaster<T> {
	private final List<T> observers;
	
	public Broadcaster() {
		observers = new ArrayList<>(32);
	}
	
	public final void registerObserver(T observer) {
		if (observer != null) {
			synchronized (observers) {
				observers.add(observer);
			}
		}
	}
	
	public final void removeObserver(T observer) {
		if (observer != null) {
			synchronized (observers) {
				observers.remove(observer);
			}
		}
	}
	
	public final void notifyObservers(Consumer<T> consumer) {
		synchronized (observers) {
			Iterator<T> it = observers.iterator();
			while (it.hasNext()) {
				T observer = it.next();
				try {
					consumer.accept(observer);
				} catch (Exception e) {
					it.remove();
					System.out.println("dropped observer");
				}
			}
		}
	}
}
