package com.windowsazure.samples.blob;

public enum BlobType {
	BLOCK_BLOB("BlockBlob"),
	PAGE_BLOB("PageBlob"),
	UNKNOWN_BLOB_TYPE("Unknown");
	
	public static BlobType fromRepresentation(String representation) {
		for (BlobType blobType : values()) {
			if (blobType.toString().equals(representation))
				return blobType;
		}
		return UNKNOWN_BLOB_TYPE;
	}
	
	@Override
	public String toString() {
		return representation;
	}
	
	private BlobType(String representation) {
		this.representation = representation;
	}
	
	private String representation;
}
