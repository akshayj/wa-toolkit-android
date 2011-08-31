package com.windowsazure.samples.internal.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.windowsazure.samples.blob.Block;
import com.windowsazure.samples.blob.BlockList;
import com.windowsazure.samples.blob.BlockStatus;
import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.NodeNotFoundException;
import com.windowsazure.samples.internal.xml.XmlNode;


public final class BlockListDOMAdapter extends AzureDOMAdapter<BlockList> {

	public BlockListDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public BlockList build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			BlockList blockList = new BlockList();
			blockList.setHttpStatusCode(statusCode);
			blockList.setErrorCode(getErrorCode());
			return blockList;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String lastModifiedText = headers.get("Last-Modified");
		String etag = headers.get("ETag");
		String contentType = headers.get(HttpHeader.CONTENT_TYPE);
		String contentLengthText = headers.get("x-ms-blob-content-length");
		String requestId = headers.get("x-ms-request-id");
		String version = headers.get("x-ms-version");
		String dateText = headers.get(HttpHeader.DATE);
		
		Date lastModified = null;
		try {lastModified = Util.gmtFormatToDate(lastModifiedText);} catch (Exception e) {}
		Integer contentLength = null;
		try {contentLength = Integer.parseInt(contentLengthText);} catch (Exception e) {}
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}

		Vector<Block> blocks = new Vector<Block>();
		blocks.addAll(getBlocks(BlockStatus.COMMITTED));
		blocks.addAll(getBlocks(BlockStatus.UNCOMMITTED));
		
		BlockList blockList = new BlockList(lastModified, etag, contentType, contentLength, requestId, version, date, blocks);
		blockList.setHttpStatusCode(statusCode);
		return blockList;
	}
	
	private Collection<Block> getBlocks(BlockStatus blockStatus)
		throws NodeNotFoundException {
		
		Vector<Block> blocks = new Vector<Block>();
		
			XmlNode blocksNode = getOptionalNode(blockStatus.getXmlNodeName());
			if (blocksNode != null) {
				Collection<XmlNode> blockNodeList = getNodeCollection(blocksNode, "Block");
				if (blockNodeList != null) {
					for (XmlNode blockNode : blockNodeList) {
						String name = getInnerText(blockNode, "Name");
						String sizeText = getInnerText(blockNode, "Size");
						Integer size = null;
						try {size = Integer.parseInt(sizeText);} catch (Exception e) {}
						blocks.add(Block.newBlock(name, blockStatus, size));
					}
				}
			}
		
		return blocks;
	}
}
