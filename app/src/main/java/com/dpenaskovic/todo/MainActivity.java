package com.dpenaskovic.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private final int REQUEST_CODE_EDIT_ITEM = 10;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReadItems();

        etNewItem = (EditText)findViewById(R.id.etNewItem);
        etNewItem.setOnClickListener(new ClickListener());

        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setOnItemLongClickListener(new ItemLongClickListener());
        lvItems.setOnItemClickListener(new ItemClickListener());

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    public void onAddItem(View v)
    {
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText(getString(R.string.enter));
        databaseSaveItem(itemsAdapter.getCount() - 1, itemText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_ITEM)
        {
            String updatedText = intent.getExtras().getString(Constants.INTENT_KEY_EDIT_TEXT);
            int position = intent.getExtras().getInt(Constants.INTENT_KEY_EDIT_POSITION, 0);

            items.remove(position);
            itemsAdapter.insert(updatedText, position);
            itemsAdapter.notifyDataSetChanged();

            databaseSaveItem(position, updatedText);
        }
    }

    private void databaseReadItems()
    {
        items = new ArrayList<>();

        List<ToDoItem> toDoItemList = SQLite.select().from(ToDoItem.class).queryList();
        for (ToDoItem toDoItem : toDoItemList)
        {
            items.add(toDoItem.name);
        }
    }

    private void databaseSaveItem(int id, String text)
    {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId(id);
        toDoItem.setName(text);
        toDoItem.save();

        System.out.println("Saved row id " + toDoItem.id + " with " + text);
    }

    private void databaseDeleteItem(int id)
    {
        // Make sure to delete this id first.
        // it could be the end of the list. make sure
        // we get it removed
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId(id);
        toDoItem.delete();

        System.out.println("Deleted row id " + toDoItem.id);
        //
        // Then write the remaining objects back out
        // so our ids are all in sync with the UI.
        //
        // This is horribly brute force. Agreed.
        // And is not an acceptable, scalable solution.
        //

        int index = 0;
        for (String name : items)
        {
            toDoItem = new ToDoItem();
            toDoItem.setId(index);
            toDoItem.setName(name);
            System.out.println("saved " + name + " at index " + index);
            toDoItem.save();
            index += 1;
        }
    }

    private class ClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            etNewItem.setText("");
        }
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            String tappedItemText = itemsAdapter.getItem(position);

            Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
            intent.putExtra(Constants.INTENT_KEY_EDIT_TEXT, tappedItemText);
            intent.putExtra(Constants.INTENT_KEY_EDIT_POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
        }
    }

    private class ItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        public boolean onItemLongClick(AdapterView<?> adapter, View item, int position, long id)
        {
            items.remove(position);
            itemsAdapter.notifyDataSetChanged();
            databaseDeleteItem(position);
            return true;
        }
    }
}
