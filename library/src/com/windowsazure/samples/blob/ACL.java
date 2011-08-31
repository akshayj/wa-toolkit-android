package com.windowsazure.samples.blob;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.windowsazure.samples.internal.util.Base64;


public class ACL {

	public static ACL newACL(Date start, Date expiry, Set<Permission> permissions) {
		return new ACL(newId(), start, expiry, permissions);
	}
	
	public static ACL newACL(String id, Date start, Date expiry, Set<Permission> permissions) {
		return new ACL(id, start, expiry, permissions);
	}
	
	public static String newId() {
		return new String(Base64.encode(UUID.randomUUID().toString().getBytes()));
	}
	
	public Date getExpiry() {
		return expiry;
	}
	
	public String getId() {
		return id;
	}
	
	public Set<Permission> getPermissions() {
		return permissions;
	}
	
	public Date getStart() {
		return start;
	}
	
	private ACL(String id, Date start, Date expiry, Set<Permission> permissions) {
		this.id = id;
		this.start = start;
		this.expiry = expiry;
		this.permissions = permissions;
	}
	
	private Date expiry;
	private String id;
	private Set<Permission> permissions;
	private Date start;
}
