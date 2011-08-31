package com.windowsazure.samples.internal.xml.tokenizer;


public abstract class AbstractTokenizer {

	public AbstractTokenizer(String input) {
		int length = input.length();
		this.input = new char[length];
		input.getChars(0, length, this.input, 0);
		this.inputText = input;
		this.position = 0;
		this.endPosition = length;
	}
	
	protected Token getChar() {
		return (position < endPosition) ?
				new Token("character", Character.toString(input[position]), position++) :
				null;
	}
	
	protected Token getChar(char expected) {
		if (position < endPosition) {
			return (input[position] == expected) ?
					new Token("character", Character.toString(input[position]), position++) :
					null;
		} else
			return null;
	}
	
	protected Token getCharInSet(String set) {
		if (position < endPosition) {
			return (set.indexOf(input[position]) != -1) ?
					new Token("character", Character.toString(input[position]), position++) :
					null;
		} else
			return null;
	}
	
	protected Token getLiteral(String name, String expected) {
		int length = expected.length();
		if (position + length < endPosition) {
			for (int i = 0; i < expected.length(); ++i) {
				if (input[position + i] != expected.charAt(i))
					return null;
			}
			int p = position;
			position += length;
			return new Token(name, expected, p);
		} else
			return null;
	}
	
	protected Token getQuotedString() {
		if (position < endPosition) {
			int p = position;
			if (input[p] == '\'' || input[p] == '"') {
				while (position < endPosition && input[++position] != input[p])
					;
				if (position < endPosition)
					return new Token("quoted-string", inputText.substring(p + 1, position++), p);
			}
		}
		
		return null;
	}
	
	protected void pushBack(Token token) {
		position = token.getPosition();
	}
	
	protected void skipTo(char expected) {
		while (position < endPosition && input[position++] != expected)
			;
		if (position < endPosition)
			--position;
	}
	
	protected void skipTo(String expected) {
		int length = expected.length();
		while (position < endPosition) {
			while (position < endPosition && input[position++] != expected.charAt(0))
				;
			if (--position + length < endPosition) {
				for (int i = 0; i < expected.length(); ++i) {
					if (input[position + i] != expected.charAt(i))
						break;
				}
				position += length;
				return;
			}
		}
	}
	
	protected void skipWhiteSpace() {
		while (position < endPosition && Character.isWhitespace(input[position]))
			++position;
	}
	
	protected char[] input;
	protected String inputText;
	protected int endPosition;
	protected int position;
}
