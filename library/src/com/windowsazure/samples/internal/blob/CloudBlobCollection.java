package com.windowsazure.samples.internal.blob;

import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.internal.web.HttpUri;


public class CloudBlobCollection implements Iterable<CloudBlob> {

	public CloudBlobCollection() {
		blobs = new Vector<CloudBlob>();
	}
	
	public void add(CloudBlob blob) {
		blobs.add(blob);
	}
	
	public HttpUri getUriForBlob(String blobName) {
		for (CloudBlob item : blobs) {
			if (item.getName().equals(blobName)) {
				String url = item.getUrl();
				String path = url.substring(url.indexOf("/usercontainer"));
				path = path.replaceAll("&amp;", "&");
				return new HttpUri(path);
			}
		}
		return null;
	}
	
	@Override
	public Iterator<CloudBlob> iterator() {
		return blobs.iterator();
	}
	
	private Vector<CloudBlob> blobs;
}
