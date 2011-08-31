package com.windowsazure.samples.sample;

import com.windowsazure.samples.sample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateDeleteSelector extends Activity
{

	int listType;
	int listSubtype;
	String blobName;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdeleteselector);
        Bundle optionSet = getIntent().getExtras();
        listType = optionSet.getInt("com.windowsazure.samples.sample.createdeleteselector.type");
        Button createButton = (Button)findViewById(R.id.CreateButton);
        Button deleteButton = (Button)findViewById(R.id.DeleteButton);
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
        	this.setTitle("Modify Tables");
        	createButton.setText("Create Table");
        	deleteButton.setText("Delete Table");
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
        {
            listSubtype = optionSet.getInt("com.windowsazure.samples.sample.createdeleteselector.subtype");
        	if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_CONTAINER)
        	{
	        	this.setTitle("Modify Containers");
	        	createButton.setText("Create Container");
	        	deleteButton.setText("Delete Container");
        	}
        	else if (listSubtype == ModifyItemDisplay.MODIFY_ITEM_SUBTYPE_BLOB)
        	{
        		blobName = optionSet.getString("com.windowsazure.samples.sample.createdeleteselector.blobname");
	        	this.setTitle("Modify Blobs");
	        	createButton.setText("Create Blob");
	        	deleteButton.setText("Delete Blob");
         	}
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
        	this.setTitle("Modify Queues");
        	createButton.setText("Create Queue");
        	deleteButton.setText("Delete Queue");
        }
        createButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {createItem();}
        	});
        deleteButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {deleteItem();}
        	});
    }

    private void createItem()
	{
    	Intent createItemDisplay = new Intent(this, CreateItemDisplay.class);
    	createItemDisplay.putExtra("com.windowsazure.samples.sample.createitemdisplay.type", listType);
        if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
        {
        	createItemDisplay.putExtra("com.windowsazure.samples.sample.createitemdisplay.subtype", listSubtype);
        	createItemDisplay.putExtra("com.windowsazure.samples.sample.createitemdisplay.containerName", blobName);
        }
    	startActivity (createItemDisplay);
	}

    private void deleteItem()
	{
    	Intent deleteItemDisplay = new Intent(this, DeleteItemDisplay.class);
    	deleteItemDisplay.putExtra("com.windowsazure.samples.sample.deleteitemdisplay.type", listType);
        if (listType == StorageTypeSelector.STORAGE_TYPE_BLOB)
        {
        	deleteItemDisplay.putExtra("com.windowsazure.samples.sample.deleteitemdisplay.subtype", listSubtype);
         	deleteItemDisplay.putExtra("com.windowsazure.samples.sample.deleteitemdisplay.blobname", blobName);
        }
    	startActivity (deleteItemDisplay);
	}

}
