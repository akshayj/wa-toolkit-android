package com.windowsazure.samples.internal.util;

public class Pair<T, V> {

	public Pair() {
		this(null, null);
	}
	
	public Pair(T first, V second) {
		this.first = first;
		this.second = second;
	}
	
	public T getFirst() {
		return first;
	}
	
	public V getSecond() {
		return second;
	}
	
	public void setFirst(T first) {
		this.first = first;
	}
	
	public void setSecond(V second) {
		this.second = second;
	}
	
	private T first;
	private V second;
}
