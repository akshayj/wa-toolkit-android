package com.windowsazure.samples.internal.blob;

import com.windowsazure.samples.blob.Block;
import com.windowsazure.samples.blob.BlockList;
import com.windowsazure.samples.internal.xml.AzureDOMBuilder;
import com.windowsazure.samples.internal.xml.XmlNode;


public class PutBlockListDOMBuilder extends AzureDOMBuilder {

	public PutBlockListDOMBuilder(BlockList blockList) {
		this.blockList = blockList;
	}
	
	@Override
	protected void buildDOM() {
		XmlNode blockListNode = addRootNode("BlockList");
		for (Block block : blockList) {
			addTextNode(blockListNode, "Latest" , block.getBlockId());
		}
	}
	
	private BlockList blockList;
}
