package com.windowsazure.samples.internal.blob;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.blob.ACL;
import com.windowsazure.samples.blob.ACLCollection;
import com.windowsazure.samples.blob.ContainerAccess;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;


final public class ACLCollectionDOMAdapter extends AzureDOMAdapter<ACLCollection> {

	public ACLCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public ACLCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			ACLCollection collection = new ACLCollection();
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String containerAccessText = headers.get("x-ms-blob-public-access");
		ContainerAccess containerAccess = ContainerAccess.fromRepresentation(containerAccessText);
		String etag = headers.get("ETag");
		String lastModifiedText = headers.get("Last-Modified");
		Date lastModified = null;
		try {lastModified = Util.gmtFormatToDate(lastModifiedText);} catch (Exception e) {}
		
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		Collection<ACL> aclCollection =
			buildCollection(getNodeCollection("SignedIdentifier"), new ACLDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		ACLCollection collection = new ACLCollection(
				containerAccess, etag, lastModified,
				requestId, version, date,
				aclCollection);
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
}
