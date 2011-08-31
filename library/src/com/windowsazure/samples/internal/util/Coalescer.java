package com.windowsazure.samples.internal.util;


public class Coalescer<T> {

	public T coalesce(T... args) {
		for (T item : args) {
			if (item != null)
				return item;
		}
		return null;
	}
	
	public T coalesce(Iterable<T> items) {
		for (T item : items) {
			if (item != null)
				return item;
		}
		return null;
	}
}
