package com.windowsazure.samples.blob;

import java.util.UUID;

import com.windowsazure.samples.internal.util.Base64;


public final class Block {

	public static Block newBlock() {
		return new Block(newBlockId(), BlockStatus.UNCOMMITTED, null);
	}
	
	public static Block newBlock(String blockId) {
		return new Block(blockId, BlockStatus.UNCOMMITTED, null);
	}
	
	public static Block newBlock(String blockId, BlockStatus status, Integer size) {
		return new Block(blockId, status, size);
	}
	
	public static String newBlockId() {
		return new String(Base64.encode(UUID.randomUUID().toString().getBytes()));
	}
	
	public String getBlockId() {
		return blockId;
	}
	
	public Integer getSize() {
		return size;
	}
	
	public boolean isCommitted() {
		return (blockStatus == BlockStatus.COMMITTED);
	}
	
	private Block(String blockId, BlockStatus blockStatus, Integer size) {
		this.blockId = blockId;
		this.blockStatus = blockStatus;
		this.size = size;
	}
	
	private String blockId;
	private BlockStatus blockStatus;
	private Integer size;
}
