package com.windowsazure.samples.blob;

public enum SnapshotDeleteAction {
	DELETE_BLOB_AND_SNAPSHOT("include"),
	DELETE_SNAPSHOT_ONLY("only");
	
	@Override
	public String toString() {
		return representation;
	}
	
	private SnapshotDeleteAction(String representation) {
		this.representation = representation;
	}
	
	private String representation;
}
