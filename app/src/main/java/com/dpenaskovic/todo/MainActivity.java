package com.dpenaskovic.todo;

import android.app.Activity;
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

public class MainActivity extends Activity implements View.OnClickListener
{
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
        etNewItem.setOnClickListener(this);

        lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v)
    {
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText(getString(R.string.enter));
        writeItems();
    }

    private void setupListViewListener()
    {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id)
            {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        etNewItem.setText("");
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
}
