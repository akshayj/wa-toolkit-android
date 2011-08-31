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
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;
import com.windowsazure.samples.queue.AzureQueueManager;
import com.windowsazure.samples.table.AzureTableManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DeleteItemDisplay extends Activity implements OnItemClickListener
{

	List<String> items = new ArrayList<String>();
	int listType;
	int listSubtype;
	int selectedRow;
	String blobName;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteitemdisplay);
        Button deleteButton = (Button)findViewById(R.id.DeleteButton);
        Bundle optionSet = getIntent().getExtras();
        listType = optionSet.getInt("com.windowsazure.samples.sample.deleteitemdisplay.type");
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
        	this.setTitle("Delete Table");
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
        {
            listSubtype = optionSet.getInt("com.windowsazure.samples.sample.deleteitemdisplay.subtype");
        	if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_CONTAINER)
        	{
        		this.setTitle("Delete Blob Container");
        	}
        	else if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_BLOB)
        	{
        		blobName = optionSet.getString("com.windowsazure.samples.sample.deleteitemdisplay.blobname");
                this.setTitle("Delete Blob");
        	}
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
        	this.setTitle("Delete Queue");
        }
        ListView listView = (ListView)findViewById(R.id.DeleteItemListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);
        deleteButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {deleteItem();}
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
        		AzureTableCollection tables = new AzureTableManager(ProxySelector.credential).queryTables();
        		Iterator<AzureTable> iterator = tables.iterator();
        		while (iterator.hasNext())
        		{
        			AzureTable table = iterator.next();
        			items.add(table.getTableName());
        		}
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
	        {
	        	if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_CONTAINER)
	        	{
	        		AzureContainerCollection containers = new AzureBlobManager(ProxySelector.credential).listAllContainers();
	        		Iterator<AzureContainer> iterator = containers.iterator();
	          		while (iterator.hasNext())
	        		{
	        			AzureContainer container = iterator.next();
	        			items.add(container.getContainerName());
	        		}
	        	}
	        	else if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_BLOB)
	        	{
	        		AzureBlobCollection blobs = new AzureBlobManager(ProxySelector.credential).listAllBlobs(blobName);
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
        		AzureQueueCollection queues = new AzureQueueManager(ProxySelector.credential).listAllQueues();
        		Iterator<AzureQueue> iterator = queues.iterator();
          		while (iterator.hasNext())
        		{
        			AzureQueue queue = iterator.next();
        			items.add(queue.getQueueName());
        		}
	        }
	        ListView table = (ListView)findViewById(R.id.DeleteItemListView);
	        table.setAdapter(new ArrayAdapter<String>(this, android.R. layout.simple_list_item_single_choice, items));
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		selectedRow = arg2;
	}

	private void deleteItem()
	{
        try
        {
	        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
	        {
	        	AzureTableManager tableWriter = new AzureTableManager(ProxySelector.credential);
	        	tableWriter.deleteTable(items.get(selectedRow));
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
	        {
	        	AzureBlobManager blobWriter = new AzureBlobManager(ProxySelector.credential);
	        	if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_CONTAINER)
	        	{
		        	blobWriter.deleteContainer(items.get(selectedRow), null);
	        	}
	        	else if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_BLOB)
	        	{
		        	blobWriter.deleteBlob(blobName, items.get(selectedRow), null, null);
	        	}
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
	        {
	        	AzureQueueManager queueWriter = new AzureQueueManager(ProxySelector.credential);
	        	queueWriter.deleteQueue(items.get(selectedRow));
	        }
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }
        finish();
	}
}
