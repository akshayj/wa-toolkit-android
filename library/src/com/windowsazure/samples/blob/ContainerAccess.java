package com.windowsazure.samples.blob;

import com.windowsazure.samples.internal.util.Util;


public enum ContainerAccess {
	
	// No public read access to container data or blobs.
	PRIVATE,
	
	// Public read access for blobs. Blob data within a container can be read via an anonymous request,
	// but the container data is not available. Clients cannot enumerate blobs within the container via
	// an anonymous request.
	BLOB,
	
	// Full public read access for container and blob data. Clients can enumerate blobs within a container
	// via an anonymous request, but cannot enumerate containers within the storage account.
	CONTAINER;
	
	public static ContainerAccess fromRepresentation(String text) {
		if (Util.isStringNullOrEmpty(text))
			return PRIVATE;
		
		return ContainerAccess.valueOf(text.toUpperCase());
	}
}
