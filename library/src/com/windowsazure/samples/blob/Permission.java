package com.windowsazure.samples.blob;

import java.util.EnumSet;
import java.util.Set;

public enum Permission {
	READ,
	WRITE,
	DELETE;
	
	public static final Set<Permission> FULL = EnumSet.allOf(Permission.class);
	
	public static Set<Permission> fromRepresentation(String text) {
		Set<Permission> permissions = EnumSet.noneOf(Permission.class);
		for (Permission permission : Permission.values()) {
			char c = permission.getRepresentation().charAt(0);
			if (text.toLowerCase().indexOf(c) != -1)
				permissions.add(permission);
		}
		return permissions;
	}
	
	public static String getRepresentation(Set<Permission> permissions) {
		String representation = "";
		for (Permission permission : permissions) {
			representation += permission.getRepresentation();
		}
		return representation;
	}
	
	public String getRepresentation() {
		return toString().substring(0, 1).toLowerCase();
	}
}
