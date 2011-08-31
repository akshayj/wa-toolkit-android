package com.windowsazure.samples.internal.blob;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.PageRangeCollection;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;


public final class PageRangeCollectionDOMAdapter extends AzureDOMAdapter<PageRangeCollection> {

	public PageRangeCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public PageRangeCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			PageRangeCollection collection = new PageRangeCollection();
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String lastModifiedText = headers.get("Last-Modified");
		String etag = headers.get("ETag");
		String contentLengthText = headers.get("x-ms-blob-content-length");
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		
		Date lastModified = null;
		try {lastModified = Util.gmtFormatToDate(lastModifiedText);} catch (Exception e) {}
		
		Integer contentLength = null;
		try {contentLength = Integer.parseInt(contentLengthText);} catch (Exception e) {}
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		Collection<BlobRange> ranges =
			buildCollection(getNodeCollection("PageRange"), new BlobRangeDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		PageRangeCollection collection = new PageRangeCollection(lastModified, etag, contentLength, requestId, version, date, ranges);
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
}
