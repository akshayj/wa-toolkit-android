package com.windowsazure.samples.internal.util;

public final class Base64 {
	
	public static byte[] decode(byte[] input) {
		int inputLength = input.length;
		byte[] output = new byte[inputLength]; // We will adjust the size after all of the characters have been inserted.
		int inputIndex = 0;
		int outputIndex = 0;
		
		for (;;) {
			
			// If there are no more input characters, build the result of the proper size and return.
			if (inputIndex >= inputLength) {
				byte[] result = new byte[outputIndex];
				System.arraycopy(output, 0, result, 0, outputIndex);
				return result;
			}
			
			// Get the next four characters from the input. (Skip over non-base64 characters.)
			
			int c1 = 0;
			while (inputIndex < inputLength) {
				c1 = input[inputIndex++];
				if (isBase64Character(c1))
					break;
			}
		
			int c2 = 0;
			while (inputIndex < inputLength) {
				c2 = input[inputIndex++];
				if (isBase64Character(c2))
					break;
			}
	
			int c3 = 0;
			while (inputIndex < inputLength) {
				c3 = input[inputIndex++];
				if (isBase64Character(c3))
					break;
			}
			
			int c4 = 0;
			while (inputIndex < inputLength) {
				c4 = input[inputIndex++];
				if (isBase64Character(c4))
					break;
			}
			
			// Decode into the four nibbles.
			Integer n1 = decodeCharacter(c1);
			Integer n2 = decodeCharacter(c2);
			Integer n3 = decodeCharacter(c3);
			Integer n4 = decodeCharacter(c4);
			
			// Assemble the bytes from the nibbles and insert into the output buffer.
			
			int d1 = ((n1 & 0x3F) << 2) | ((n2 & 0x3F) >>> 4);
			output[outputIndex++] = (byte) d1;
			
			if (n3 != null) {
				int d2 = ((n2 & 0x0F) << 4) | ((n3 & 0x3F) >>> 2);
				output[outputIndex++] = (byte) d2;
			}
			
			if (n4 != null) {
				int d3 = ((n3 & 0x03F) << 6) | (n4 & 0x3F);
				output[outputIndex++] = (byte) d3;
			}
		}
	}

	public static byte[] encode(byte[] input) {
		int inputLength = input.length;
		int paddedInputLength = (inputLength % 3 == 0) ? inputLength : inputLength + 3 - (inputLength % 3);
		
		int outputLength = (paddedInputLength / 3) * 4;
		int numberOfOutputLines = outputLength / LINE_LENGTH;
		if (numberOfOutputLines > 1 && outputLength % LINE_LENGTH == 0)
			--numberOfOutputLines;
		outputLength += 2 * numberOfOutputLines;  // Adjust for \r\n between lines.
		
		byte[] output = new byte[outputLength];
		int inputIndex = 0;
		int outputIndex = 0;
		int nextOutputLineBreak = LINE_LENGTH;
		
		for (;;) {
			
			// Get the next three characters from the input.
			Integer c1 = (inputIndex < inputLength) ? (int) input[inputIndex++] : null;
			Integer c2 = (inputIndex < inputLength) ? (int) input[inputIndex++] : null;
			Integer c3 = (inputIndex < inputLength) ? (int) input[inputIndex++] : null;
			
			// If there are no more characters we are done.
			if (c1 == null)
				return output;
			
			// Turn the three (nullable) characters into bytes for the transformation.
			int b1 = c1;
			int b2 = (c2 != null) ? c2 : 0;
			int b3 = (c3 != null) ? c3 : 0;
			
			// Extract the four 6-bit nibbles from the three bytes.
			int n1 = ((b1 & 0xFF) >>> 2);
			int n2 = ((b1 & 0x03) << 4) | ((b2 & 0xFF) >>> 4);
			int n3 = ((b2 & 0x0F) << 2) | ((b3 & 0xFF)  >>> 6);
			int n4 = (b3 & 0x3F);
			
			// Encode the nibbles (if there were null input characters they get encoded as '='.)
			int e1 = encodeCharacter(n1);
			int e2 = encodeCharacter(n2);
			int e3 = encodeCharacter((c2 != null) ? n3 : null);
			int e4 = encodeCharacter((c3 != null) ? n4 : null);
			
			// Put the encoded characters into the output buffer.
			output[outputIndex++] = (byte) e1;
			output[outputIndex++] = (byte) e2;
			output[outputIndex++] = (byte) e3;
			output[outputIndex++] = (byte) e4;
			
			// Insert a \r\n after every 76 characters.
			if (outputIndex >= nextOutputLineBreak) {
				output[outputIndex++] = '\r';
				output[outputIndex++] = '\n';
				nextOutputLineBreak = outputIndex + LINE_LENGTH;
			}
		}
	}
	
	private Base64() {}
	
	private static Integer decodeCharacter(int c) {
		return (c != '=') ? (int) DECODE[c] : null;
	}
	
	private static int encodeCharacter(Integer c) {
		return (int) ((c != null) ? ENCODE[c] : '=');
	}
	
	private static boolean isBase64Character(int c) {
		return (DECODE[c] != ILLEGAL_CHARACTER || c == '=');
	}
	
	private static final byte[] DECODE;
	private static final byte[] ENCODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();
	private static final int ILLEGAL_CHARACTER = 64; /* Base-64 uses values 0-63 */
	private static final int LINE_LENGTH = 76;
	
	// Build the DECODE mapping array. There is one entry for every possible 8-bit value. For the characters defined by the
	// base-64 encoding, those entries are set to the numerical value of the base-64 character. For all other entries, an
	// illegal value is stored.
	static {
		byte[] decode = new byte[256];
		for (int i = 0; i < 256; ++i )
			decode[i] = (byte) ILLEGAL_CHARACTER;
		for (int i = 0; i < 64; ++i)
			decode[ENCODE[i]] = (byte) i;
		DECODE = decode;
	}
}
