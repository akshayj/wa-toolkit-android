package com.windowsazure.samples.internal.blob;

import java.util.Date;
import java.util.Set;

import com.windowsazure.samples.blob.ACL;
import com.windowsazure.samples.blob.Permission;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;


final class ACLDOMAdapter extends AzureDOMAdapter<ACL> {

	@Override
	public ACL build()
		throws Exception {
		
		String id = getInnerText("Id");
		
		XmlNode accessPolicyNode = getNode("AccessPolicy");
		
		String startText = getInnerText(accessPolicyNode, "Start");
		Date start = Util.xmlStringToDate(startText);
		
		String expiryText = getInnerText(accessPolicyNode, "Expiry");
		Date expiry = Util.xmlStringToDate(expiryText);
		
		String permissionText = getInnerText(accessPolicyNode, "Permission");
		Set<Permission> permissions = Permission.fromRepresentation(permissionText);
		
		return ACL.newACL(id, start, expiry, permissions);
	}
	
	protected ACLDOMAdapter(XmlHttpResult result) {
		super(result);
	}
}
