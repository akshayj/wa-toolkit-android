package com.windowsazure.samples.blob;

public final class BlobRange {

	public static BlobRange fromStartAndEnd(int start, int end)
		throws IllegalBlobRangeException {
		
		return new BlobRange(start, end);
	}
	
	public static BlobRange fromStartAndLength(int start, int length)
		throws IllegalBlobRangeException {
		
		return new BlobRange(start, start + length - 1);
	}
	
	public static BlobRange fromString(String text)
		throws Exception {
		
		while (! Character.isDigit(text.charAt(0)))
			text = text.substring(1);
		String[] parts = text.split("-");
		return new BlobRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	}
	
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof BlobRange))
			return false;
		BlobRange rhs = (BlobRange) o;
		return (start == rhs.start) && (end == rhs.end);
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getLength() {
		return end - start + 1;
	}
	
	public int getStart() {
		return start;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "bytes=" + start + "-" + end;
	}
	
	private BlobRange(int start, int end)
		throws IllegalBlobRangeException {
		
		if (start < 0 ||
			end < 0 ||
			end <= start ||
			start % 512 != 0 ||
			end % 511 != 0)
			throw new IllegalBlobRangeException();
		
		this.start = start;
		this.end = end;
	}
	
	private int end;
	private int start;
}
