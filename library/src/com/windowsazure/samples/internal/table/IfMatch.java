package com.windowsazure.samples.internal.table;

import java.util.Vector;


public final class IfMatch {

	public static final IfMatch WILD = new IfMatch("*");
	
	public IfMatch() {}
	
	public IfMatch(String... args) {
		for (String pattern : args) {
			addPattern(pattern);
		}
	}
	
	public void addPattern(String pattern) {
		if (! hasPattern(pattern))
			patterns.add(pattern);
	}
	
	public String getRepresentation() {
		if (hasPattern("*"))
			return "*";
		
		StringBuilder sb = new StringBuilder();
		for (String pattern : patterns) {
			sb.append(pattern);
			sb.append(", ");
		}
		
		String result = sb.toString();
		return result.substring(0, result.length() - 1);
	}
	
	private boolean hasPattern(String pattern) {
		for (String item : patterns) {
			if (item.equals(pattern))
				return true;
		}
		return false;
	}
	
	private Vector<String> patterns = new Vector<String>();
}
