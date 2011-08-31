package com.windowsazure.samples.blob.data;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.internal.util.Base64;


public class BitmapBlobData extends BlobData {

	public static BitmapBlobData fromBlob(AzureBlob blob) {
		BitmapBlobData blobData = new BitmapBlobData();
		blobData.cacheControl = blob.getCacheControl();
		blobData.contentEncoding = blob.getContentEncoding();
		blobData.contentLanguage = blob.getContentLanguage();
		blobData.contentMd5 = blob.getContentMd5();
		blobData.contentType = blob.getContentType();
		blobData.decode(blob.getServerData());
		return blobData;
	}
	
	public BitmapBlobData() {
		this(null);
	}
	
	public BitmapBlobData(Bitmap bitmap) {
		this.contentEncoding = "base64";
		this.contentType = "application/octet-stream";
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	@Override
	public Integer GetContentLength() {
		return Base64.encode(getBytes()).length;
	}
	
	@Override
	public String toString() {
		return bitmap.toString();
	}
	
	@Override
	public void decode(String serverData) {
		byte[] data = Base64.decode(serverData.getBytes());
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	@Override
	public String encode() {
		return new String(Base64.encode(getBytes()));
	}
	
	private byte[] getBytes() {
		if (bitmap == null)
			return new byte[0];
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
		return baos.toByteArray();
	}

	private Bitmap bitmap;
}
