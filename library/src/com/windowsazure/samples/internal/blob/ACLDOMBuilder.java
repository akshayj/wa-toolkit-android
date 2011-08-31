package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.blob.ACL;
import com.windowsazure.samples.blob.ACLCollection;
import com.windowsazure.samples.blob.Permission;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.xml.AzureDOMBuilder;
import com.windowsazure.samples.internal.xml.XmlNode;


public class ACLDOMBuilder extends AzureDOMBuilder {

	public ACLDOMBuilder(ACLCollection aclCollection) {
		this.aclCollection = aclCollection;
	}
	
	@Override
	protected void buildDOM() {
		XmlNode signedIdentifiersNode = addRootNode("SignedIdentifiers");
		removeHeaderAttribute("standalone");
		
		for (ACL acl : aclCollection) {
			XmlNode signedIdentifierNode = addNode(signedIdentifiersNode, "SignedIdentifier");
			addTextNode(signedIdentifierNode, "Id", acl.getId());
			
			XmlNode accessPolicyNode = addNode(signedIdentifierNode, "AccessPolicy");
			addTextNode(accessPolicyNode, "Start", getFormattedDate(acl.getStart()));
			addTextNode(accessPolicyNode, "Expiry", getFormattedDate(acl.getExpiry()));
			addTextNode(accessPolicyNode, "Permission", Permission.getRepresentation(acl.getPermissions()));
		}
	}
	
	private String getFormattedDate(Date date) {
		String text = Util.dateToXmlStringWithoutTZ(date);
		text += "Z";
		return text;
	}
	
	private ACLCollection aclCollection;
}
