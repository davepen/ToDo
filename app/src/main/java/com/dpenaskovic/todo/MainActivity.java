package com.dpenaskovic.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

        readItems();

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
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_ITEM)
        {
            String updatedText = intent.getExtras().getString("text");
            int position = intent.getExtras().getInt("position", 0);

            items.remove(position);
            itemsAdapter.insert(updatedText, position);
            itemsAdapter.notifyDataSetChanged();

            writeItems();
        }
    }

    private void readItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try
        {
            items = new ArrayList<>(FileUtils.readLines(todoFile, "UTF-8"));
        }
        catch (IOException ex)
        {
            items = new ArrayList<>();
        }
    }

    private void writeItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try
        {
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
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
            intent.putExtra("text", tappedItemText);
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
        }
    }

    private class ItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id)
        {
            items.remove(pos);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            return true;
        }
    }
}
