package com.windowsazure.samples.internal.blob;

import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;


final class BlobRangeDOMAdapter extends AzureDOMAdapter<BlobRange> {

	protected BlobRangeDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public BlobRange build()
		throws Exception {
		
		String startText = getInnerText("Start");
		String endText = getInnerText("End");
		
		int start = Integer.parseInt(startText);
		int end = Integer.parseInt(endText);
		
		return BlobRange.fromStartAndEnd(start, end);
	}
}
