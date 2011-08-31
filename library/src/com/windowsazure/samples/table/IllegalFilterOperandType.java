package com.windowsazure.samples.table;


public class IllegalFilterOperandType extends Exception {

	public IllegalFilterOperandType() {
		super("A filter operand must be a Boolean, Date, Double, Float, Integer, Long, String, or UUID.");
	}
	
	private static final long serialVersionUID = -4834948234506195098L;
}
