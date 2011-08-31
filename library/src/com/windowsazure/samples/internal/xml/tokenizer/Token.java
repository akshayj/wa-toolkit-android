package com.windowsazure.samples.internal.xml.tokenizer;


public class Token {
	
	public Token(String name, String literal, int position) {
		this.name = name;
		this.literal = literal;
		this.position = position;
	}
	
	public char getAsChar() {
		return literal.charAt(0);
	}
	
	public String getName() {
		return name;
	}
	
	public int getPosition() {
		return position;
	}
	
	public String getRepresentation() {
		return literal;
	}
	
	private String literal;
	private String name;
	private int position;
}
