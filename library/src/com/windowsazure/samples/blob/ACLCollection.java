package com.windowsazure.samples.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public class ACLCollection extends EntityBase implements Iterable<ACL> {

	public ACLCollection() {
		this(
		    null, null, null,
		    null, null, null,
		    new Vector<ACL>());
	}
	
	public ACLCollection(
			ContainerAccess containerAccess, String etag, Date lastModified,
			String requestId, String version, Date date,
			Collection<ACL> aclCollection) {
		
		this.containerAccess = containerAccess;
		this.etag = etag;
		this.lastModified = lastModified;
		
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		
		this.aclVector = new Vector<ACL>(aclCollection);
	}
	
	public void add(ACL acl) {
		aclVector.add(acl);
	}
	
	public int getAclCount() {
		return aclVector.size();
	}
	
	public ContainerAccess getContainerAccess() {
		return containerAccess;
	}
	
	public String getContainerName() {
		return containerName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getEtag() {
		return etag;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<ACL> iterator() {
		return aclVector.iterator();
	}
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	private Vector<ACL> aclVector;
	private String containerName;
	private ContainerAccess containerAccess;
	private Date date;
	private String etag;
	private Date lastModified;
	private String requestId;
	private String version;
}
