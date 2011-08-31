package com.windowsazure.samples.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public class PageRangeCollection extends EntityBase implements Iterable<BlobRange> {
	
	public PageRangeCollection() {
		this(null, null, null, null, null, null, new Vector<BlobRange>());
	}
	
	public PageRangeCollection(
			Date lastModified,
			String etag,
			Integer contentLength,
			String requestId,
			String version,
			Date date,
			Collection<BlobRange> ranges) {
		this.lastModified = lastModified;
		this.etag = etag;
		this.contentLength = contentLength;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		this.ranges = ranges;
	}
	
	public Integer getContentLength() {
		return contentLength;
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
	
	public Collection<BlobRange> getRanges() {
		return ranges;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<BlobRange> iterator() {
		return ranges.iterator();
	}
	
	private Integer contentLength;
	private Date date;
	private String etag;
	private Date lastModified;
	private Collection<BlobRange> ranges;
	private String requestId;
	private String version;
}
