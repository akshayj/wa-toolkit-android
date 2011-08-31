package com.windowsazure.samples.internal.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.AzureBlobCollection;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;


public final class BlobCollectionDOMAdapter extends AzureDOMAdapter<AzureBlobCollection> {

	public BlobCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureBlobCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureBlobCollection collection = new AzureBlobCollection(null);
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		setter.clear();
		
		setter.setRequestId(headers.get(AzureHttpHeader.XMS_REQUEST_ID));
		setter.setVersion(headers.get(AzureHttpHeader.XMS_VERSION));
		String dateString = headers.get(HttpHeader.DATE);
		if (! Util.isStringNullOrEmpty(dateString)) {
			Date date = null;
			try {date = Util.gmtFormatToDate(dateString);} catch (Exception e) {}
			setter.setDate(date);
		}
		
		setter.setPrefix(getOptionalInnerText("Prefix"));
		setter.setMarker(getOptionalInnerText("Marker"));
		
		String maxResultsText = getOptionalInnerText("MaxResults");
		if (! Util.isStringNullOrEmpty(maxResultsText))
			setter.setMaxResults(Integer.parseInt(maxResultsText));
		
		setter.setDelimiter(getOptionalInnerText("Delimiter"));
		
		XmlNode blobsNode = getNode("Blobs");
		Collection<AzureBlob> blobs =
			buildCollection(getNodeCollection(blobsNode, "Blob"), new BlobDOMAdapter(xmlHttpResult.getEmptyResult()));
		setter.setBlobs((Vector<AzureBlob>) blobs);
		
		XmlNode blobPrefixNode = getOptionalNode("BlobPrefix");
		String blobPrefix = (blobPrefixNode != null) ? getOptionalInnerText(blobPrefixNode, "Name") : null;
		setter.setBlobPrefix(blobPrefix);
		
		setter.setNextMarker(getOptionalInnerText("NextMarker"));
		
		AzureBlobCollection collection = new AzureBlobCollection(setter);
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
	
	private static final AzureBlobCollectionSetter setter = new AzureBlobCollectionSetter();
}
