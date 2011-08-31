package com.windowsazure.samples.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.windowsazure.samples.sample.R;
import com.windowsazure.samples.Property;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.queue.AzureQueueMessage;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;
import com.windowsazure.samples.queue.AzureQueueManager;
import com.windowsazure.samples.table.AzureTableManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;

public class EntityList extends Activity implements OnChildClickListener
{

	List<List<Map<String, String>>> items = new ArrayList<List<Map<String, String>>>();
	String entityName = null;
	int listType = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entitylist);
        Button addButton = (Button)findViewById(R.id.AddItemButton);
        Bundle optionSet = getIntent().getExtras();
        listType = optionSet.getInt("com.windowsazure.samples.sample.entitylist.type");
        entityName = optionSet.getString("com.windowsazure.samples.sample.entitylist.title");
        this.setTitle(optionSet.getString("com.windowsazure.samples.sample.entitylist.title"));
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.EntityListView);
        listView.setOnChildClickListener(this);
        addButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {addItem();}
        	});
   }

	public void onStart()
	{
		super.onStart();
		items.clear();
        try
        {
	        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
	        {
        		AzureTableEntityCollection entities = new AzureTableManager(ProxySelector.credential).queryAllEntities((String)this.getTitle());
        		Iterator<AzureTableEntity> entityIterator = entities.iterator();
        		while (entityIterator.hasNext())
        		{
        			Map<String, String> groupFields = new HashMap<String, String>();
	       			List<Map<String, String>> entry = new ArrayList<Map<String, String>>();
        			AzureTableEntity entity = entityIterator.next();
        			PropertyCollection properties = entity.getProperties();
            		Iterator<Property<?>> propertyIterator = properties.iterator();
            		while (propertyIterator.hasNext())
		       		{
            			Property<?> property = propertyIterator.next();
		    			Map<String, String> lineItem = new HashMap<String, String>();
			       		lineItem.put("Name", property.getName());
			       		lineItem.put("Value", property.getValue().toString());
		       			entry.add(lineItem);
		       		}
		       		groupFields.put("PartitionKey", entity.getPartitionKey());
		       		groupFields.put("RowKey", entity.getRowKey());
			    	items.add(entry);
		       		groupData.add(groupFields);
        		}
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
	        {
        		AzureQueueMessageCollection messages = new AzureQueueManager(ProxySelector.credential).peekMessages((String)this.getTitle(), 32);
        		Iterator<AzureQueueMessage> iterator = messages.iterator();
          		while (iterator.hasNext())
        		{
        			Map<String, String> groupFields = new HashMap<String, String>();
	       			List<Map<String, String>> entry = new ArrayList<Map<String, String>>();
        			AzureQueueMessage message = iterator.next();
	    			Map<String, String> lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Message ID");
		       		lineItem.put("Value", message.getMessageId());
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Insetion Time");
		       		if (message.getInsertionTime() != null)
		       		{
		       			lineItem.put("Value", message.getInsertionTime().toString());
		       		}
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Expiration Time");
		       		if (message.getExpirationTime() != null)
		       		{
		       			lineItem.put("Value", message.getExpirationTime().toString());
		       		}
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Pop Receipt");
		       		lineItem.put("Value", message.getPopReceipt());
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Time Next Visible");
		       		if (message.getTimeNextVisible() != null)
		       		{
		       			lineItem.put("Value", message.getTimeNextVisible().toString());
		       		}
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Message");
		       		lineItem.put("Value", message.getMessageText());
	       			entry.add(lineItem);
		       		groupFields.put("Message", message.getMessageText());
		       		groupData.add(groupFields);
			    	items.add(entry);
        		}
	        }
	       	ExpandableListView table = (ExpandableListView)findViewById(R.id.EntityListView);
	        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
	        {
	        	table.setAdapter(new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] {"PartitionKey", "RowKey"}, new int[] {android.R.id.text1, android.R.id.text2}, items, android.R.layout.simple_expandable_list_item_2, new String[] {"Name", "Value"}, new int[] {android.R.id.text1, android.R.id.text2}));
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
	        {
	        	table.setAdapter(new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] {"Message"}, new int[] {android.R.id.text1}, items, android.R.layout.simple_expandable_list_item_2, new String[] {"Name", "Value"}, new int[] {android.R.id.text1, android.R.id.text2}));
	        }
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }
	}

    private void addItem()
	{
    	Intent addItemDisplay = new Intent(this, ModifyItemDisplay.class);
    	addItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.mode", ModifyItemDisplay.MODIFY_ITEM_MODE_ADD);
    	addItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.type", listType);
    	addItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.listName", entityName);
    	startActivity (addItemDisplay);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
    	Intent updateItemDisplay = new Intent(this, ModifyItemDisplay.class);
    	updateItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.mode", ModifyItemDisplay.MODIFY_ITEM_MODE_UPDATE);
    	updateItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.selection", groupPosition);
    	updateItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.type", listType);
    	updateItemDisplay.putExtra("com.windowsazure.samples.sample.modifyitemdisplay.listName", entityName);
    	startActivity (updateItemDisplay);
		return true;
	}
}
