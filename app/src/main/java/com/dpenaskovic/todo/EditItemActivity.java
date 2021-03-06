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
    int editItemArrayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItem = (EditText)findViewById(R.id.editText2);
        buttonSave = (Button)findViewById(R.id.button);
        buttonSave.setOnClickListener(new SaveButtonClickListener());

        String textToEdit = getIntent().getStringExtra(Constants.INTENT_KEY_EDIT_TEXT);
        editItemArrayPosition = getIntent().getIntExtra(Constants.INTENT_KEY_EDIT_POSITION, 0);

        etItem.setText(textToEdit);
        etItem.setSelection(textToEdit.length());
    }

    private class SaveButtonClickListener implements View.OnClickListener
    {
        public void onClick(View view)
        {
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_KEY_EDIT_TEXT, etItem.getText().toString());
            intent.putExtra(Constants.INTENT_KEY_EDIT_POSITION, editItemArrayPosition);

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
