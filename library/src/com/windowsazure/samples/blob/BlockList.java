package com.windowsazure.samples.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public class BlockList extends EntityBase implements Iterable<Block> {

	public BlockList() {
		this(null, null, null, null, null, null, null, new Vector<Block>());
	}
	
	public BlockList(
			Date lastModified,
			String etag,
			String contentType,
			Integer contentLength,
			String requestId,
			String version,
			Date date,
			Collection<Block> blocks) {
		this.lastModified = lastModified;
		this.etag = etag;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		this.blocks = new Vector<Block>(blocks);
	}
	
	public void add(Block block) {
		blocks.add(block);
	}
	
	public Collection<Block> getAllBlocks() {
		return blocks;
	}
	
	public Collection<Block> getCommittedBlocks() {
		Vector<Block> committedBlocks = new Vector<Block>();
		for (Block block : blocks) {
			if (block.isCommitted())
				committedBlocks.add(block);
		}
		return committedBlocks;
	}
	
	public Integer getContentLength() {
		return contentLength;
	}
	
	public String getContentType() {
		return contentType;
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
	
	public Collection<Block> getUncommittedBlocks() {
		Vector<Block> uncommittedBlocks = new Vector<Block>();
		for (Block block : blocks) {
			if (! block.isCommitted())
				uncommittedBlocks.add(block);
		}
		return uncommittedBlocks;
	}
	
	public String getVersion() {
		return version;
	}
	
	public boolean hasCommittedBlocks() {
		return (getCommittedBlocks().size() > 0);
	}
	
	public boolean hasUncommittedBlocks() {
		return (getUncommittedBlocks().size() > 0);
	}
	
	@Override
	public Iterator<Block> iterator() {
		return blocks.iterator();
	}
	
	private Vector<Block> blocks;
	private Integer contentLength;
	private String contentType;
	private Date date;
	private String etag;
	private Date lastModified;
	private String requestId;
	private String version;
}
