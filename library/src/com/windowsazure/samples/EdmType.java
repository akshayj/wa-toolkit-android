package com.windowsazure.samples;

import com.windowsazure.samples.EdmType;


public enum EdmType {
	EdmBinary("Edm.Binary"),
	EdmBoolean("Edm.Boolen"),
	EdmDateTime("Edm.DateTime"),
	EdmDouble("Edm.Double"),
	EdmGuid("Edm.Guid"),
	EdmInt32("Edm.Int32"),
	EdmInt64("Edm.Int64"),
	EdmString("Edm.String"),
	EdmUnsupported("Unsupported Type");
	
	public static EdmType fromRepresentation(String representation)
		throws Exception {
	
		for (EdmType type : values()) {
			if (type.toString().equals(representation))
				return type;
		}
	
		throw new Exception("Invalid EdmType " + representation);
	}
	
	@Override
	public String toString() {
		return edmRepresentation;
	}
	
	private EdmType(String representation) {
		this.edmRepresentation = representation;
	}
	
	private String edmRepresentation;
}
