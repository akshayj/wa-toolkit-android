package com.windowsazure.samples.internal.blob;

import java.util.Collection;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;


public class SharedSignatureServiceResponseDOMAdapter extends AzureDOMAdapter<SharedSignatureServiceResponse> {

	protected SharedSignatureServiceResponseDOMAdapter(XmlHttpResult result) {
		super(result);
	}

	@Override
	public SharedSignatureServiceResponse build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			SharedSignatureServiceResponse response = new SharedSignatureServiceResponse();
			response.setHttpStatusCode(statusCode);
			return response;
		}
		
		String anyUrl = null;
		
		CloudBlobCollection blobCollection = new CloudBlobCollection();
		XmlNode blobsNode = getOptionalNode("Blobs");
		if (blobsNode != null) {
			Collection<XmlNode> blobList = getNodeCollection(blobsNode, "Blob");
			for (XmlNode blobNode : blobList) {
				String name = getInnerText(blobNode, "Name");
				String url = getInnerText(blobNode, "Url");
				blobCollection.add(new CloudBlob(name, url));
			}
		}
		else {
			anyUrl = getRootNode().getInnerText();
			if (! Util.isStringNullOrEmpty(anyUrl))
				anyUrl = anyUrl.replaceAll("&amp;", "&");
		}
		
		SharedSignatureServiceResponse response = new SharedSignatureServiceResponse(anyUrl, blobCollection);
		response.setHttpStatusCode(statusCode);
		return response;
	}
}
