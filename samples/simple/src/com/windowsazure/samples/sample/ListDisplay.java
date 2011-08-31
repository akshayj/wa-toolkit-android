package com.windowsazure.samples.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.windowsazure.samples.sample.R;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.AzureBlobCollection;
import com.windowsazure.samples.blob.AzureBlobManager;
import com.windowsazure.samples.blob.AzureContainer;
import com.windowsazure.samples.blob.AzureContainerCollection;
import com.windowsazure.samples.queue.AzureQueue;
import com.windowsazure.samples.queue.AzureQueueCollection;
import com.windowsazure.samples.queue.AzureQueueMessage;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;
import com.windowsazure.samples.queue.AzureQueueManager;
import com.windowsazure.samples.table.AzureTableManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ListDisplay extends Activity implements OnItemClickListener
{

	static int FONT_SIZE = 18;
	static int ROW_SIZE = 36;

	List<String> items = new ArrayList<String>();
	int listType = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdisplay);
        Button modifyButton = (Button)findViewById(R.id.ModifyButton);
        Bundle optionSet = getIntent().getExtras();
        listType = optionSet.getInt("com.windowsazure.samples.sample.listdisplay.type");
        this.setTitle(optionSet.getString("com.windowsazure.samples.sample.listdisplay.title"));
        ListView listView = (ListView)findViewById(R.id.ListDisplayListView);
        listView.setOnItemClickListener(this);
        modifyButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {modifyStorage();}
        	});
    }

	public void onStart()
	{
		super.onStart();
		items.clear();
        try
        {
	        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
	        {
	        	if (this.getTitle().equals("Table Storage"))
	        	{
	        		AzureTableCollection tables = new AzureTableManager(ProxySelector.credential).queryTables();
	        		Iterator<AzureTable> iterator = tables.iterator();
	        		while (iterator.hasNext())
	        		{
	        			AzureTable table = iterator.next();
	        			items.add(table.getTableName());
	        		}
	        	}
	        	else
	        	{
	        		AzureTableEntityCollection entities = new AzureTableManager(ProxySelector.credential).queryAllEntities((String)this.getTitle());
	        		Iterator<AzureTableEntity> iterator = entities.iterator();
	        		while (iterator.hasNext())
	        		{
	        			AzureTableEntity entity = iterator.next();
	        			items.add(entity.getTitle());
	        		}
	        	}
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
	        {
	        	if (this.getTitle().equals("Blob Storage"))
	        	{
	        		AzureContainerCollection containers = new AzureBlobManager(ProxySelector.credential).listAllContainers();
	        		Iterator<AzureContainer> iterator = containers.iterator();
	          		while (iterator.hasNext())
	        		{
	        			AzureContainer container = iterator.next();
	        			items.add(container.getContainerName());
	        		}
	        	}
	        	else
	        	{
	        		AzureBlobCollection blobs = new AzureBlobManager(ProxySelector.credential).listAllBlobs((String)this.getTitle());
	        		Iterator<AzureBlob> iterator = blobs.iterator();
	          		while (iterator.hasNext())
	        		{
	        			AzureBlob blob = iterator.next();
	        			items.add(blob.getBlobName());
	        		}
	        	}
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
	        {
	        	if (this.getTitle().equals("Queue Storage"))
	        	{
	        		AzureQueueCollection queues = new AzureQueueManager(ProxySelector.credential).listAllQueues();
	        		Iterator<AzureQueue> iterator = queues.iterator();
	          		while (iterator.hasNext())
	        		{
	        			AzureQueue queue = iterator.next();
	        			items.add(queue.getQueueName());
	        		}
	        	}
	        	else
	        	{
	        		AzureQueueMessageCollection messages = new AzureQueueManager(ProxySelector.credential).peekMessages((String)this.getTitle(), 32);
	        		Iterator<AzureQueueMessage> iterator = messages.iterator();
	          		while (iterator.hasNext())
	        		{
	        			AzureQueueMessage message = iterator.next();
	        			items.add(message.getMessageText());
	        		}
	        	}
	        }
	    	ListView table = (ListView)findViewById(R.id.ListDisplayListView);
	        table.setAdapter(new ArrayAdapter<String>(this, android.R. layout.simple_list_item_single_choice, items));
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
            Intent launchEntityListDisplay = new Intent(this, EntityList.class);
            launchEntityListDisplay.putExtra("com.windowsazure.samples.sample.entitylist.type", StorageTypeSelector.STORAGE_TYPE_TABLE);
           	launchEntityListDisplay.putExtra("com.windowsazure.samples.sample.entitylist.title", items.get(arg2));
           	startActivity (launchEntityListDisplay);
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
        {
        	if (this.getTitle().equals("Blob Storage"))
        	{
            	Intent launchBlobDisplay = new Intent(this, ListDisplay.class);
            	launchBlobDisplay.putExtra("com.windowsazure.samples.sample.listdisplay.type", StorageTypeSelector.STORAGE_TYPE_BLOB);
            	launchBlobDisplay.putExtra("com.windowsazure.samples.sample.listdisplay.title", items.get(arg2));
            	startActivity (launchBlobDisplay);
        	}
        	else
        	{
            	Intent launchBlobViewer = new Intent(this, BlobViewer.class);
            	launchBlobViewer.putExtra("com.windowsazure.samples.sample.blobviewer.container", this.getTitle());
            	launchBlobViewer.putExtra("com.windowsazure.samples.sample.blobviewer.blob", items.get(arg2));
            	startActivity (launchBlobViewer);
        	}
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
            Intent launchQueueMessageDisplay = new Intent(this, EntityList.class);
            launchQueueMessageDisplay.putExtra("com.windowsazure.samples.sample.entitylist.type", StorageTypeSelector.STORAGE_TYPE_QUEUE);
            launchQueueMessageDisplay.putExtra("com.windowsazure.samples.sample.entitylist.title", items.get(arg2));
           	startActivity (launchQueueMessageDisplay);
        }
	}

    private void modifyStorage()
	{
    	Intent launchCreateDeleteSelector = new Intent(this, CreateDeleteSelector.class);
    	launchCreateDeleteSelector.putExtra("com.windowsazure.samples.sample.createdeleteselector.type", listType);
        if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
        {
        	if (this.getTitle().equals("Blob Storage"))
        	{
            	launchCreateDeleteSelector.putExtra("com.windowsazure.samples.sample.createdeleteselector.subtype", ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_CONTAINER);
        	}
        	else
        	{
            	launchCreateDeleteSelector.putExtra("com.windowsazure.samples.sample.createdeleteselector.subtype", ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_BLOB);
            	launchCreateDeleteSelector.putExtra("com.windowsazure.samples.sample.createdeleteselector.blobname", this.getTitle());
        	}
        }
    	startActivity (launchCreateDeleteSelector);
	}

}
