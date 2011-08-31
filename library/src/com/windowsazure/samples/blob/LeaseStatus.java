package com.windowsazure.samples.blob;


public enum LeaseStatus {
	LOCKED,
	UNLOCKED,
	UNKNOWN_LEASE_STATUS;
	
	public static LeaseStatus fromString(String text) {
		for (LeaseStatus leaseStatus : values()) {
			if (leaseStatus.toString().equalsIgnoreCase(text))
				return leaseStatus;
		}
		return UNKNOWN_LEASE_STATUS;
	}
}
