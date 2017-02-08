package com.dpenaskovic.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity
{
    EditText etItem;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);


        etItem = (EditText)findViewById(R.id.editText2);
        buttonSave = (Button)findViewById(R.id.button);
        buttonSave.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent data = new Intent();
                data.putExtra("text", etItem.getText().toString());
                data.putExtra("code", 200);

                // Activity finished ok, return the data
                setResult(RESULT_OK, data);
                finish();
            }
        });

        String textToEdit = getIntent().getStringExtra("text");
        int position = getIntent().getIntExtra("position", 0);

        etItem.setText(textToEdit);
        etItem.setSelection(textToEdit.length());

        System.out.println(textToEdit);
        System.out.println(position);
    }
}
