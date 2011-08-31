package com.windowsazure.samples.sample;

import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.AzureBlobManager;
import com.windowsazure.samples.blob.data.BitmapBlobData;
import com.windowsazure.samples.sample.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class BlobViewer extends Activity
{

	String containerName = null;
	String blobName = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blobviewer);
        Bundle optionSet = getIntent().getExtras();
        containerName = optionSet.getString("com.windowsazure.samples.sample.blobviewer.container");
        blobName = optionSet.getString("com.windowsazure.samples.sample.blobviewer.blob");
        this.setTitle(optionSet.getString("com.windowsazure.samples.sample.blobviewer.blob"));
    }

	public void onStart()
	{
		super.onStart();
		ImageView imageView = (ImageView)findViewById(R.id.BlobImageView);
		try
		{
    		AzureBlob blob = new AzureBlobManager(ProxySelector.credential).getBlob(containerName, blobName, null, null, null, null);
	        Bitmap bitmap = BitmapBlobData.fromBlob(blob).getBitmap();
	        imageView.setImageBitmap(bitmap);
		}
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }
	}
}
