package com.sudhishkr.codepath.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditTaskActivity extends AppCompatActivity {

    DBHandle db;
    Bundle bundle;
    Boolean checkEditOnTask = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        db = new DBHandle(this);

        bundle = getIntent().getExtras();

        final EditText editTextEditTask = (EditText) findViewById(R.id.editTextEditTask);
        editTextEditTask.setText(bundle.getString(MainActivity.BUNDLE_TASK_NAME));
        editTextEditTask.setSelection(editTextEditTask.getText().length());

        Button buttonEditSave = (Button)findViewById(R.id.buttonEditSave);
        buttonEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEditOnTask = Boolean.TRUE;
                if (db.updateTask(bundle.getString(MainActivity.BUNDLE_TASK_NAME).toString(), editTextEditTask.getText().toString())){
                    Toast.makeText(getApplicationContext(), "updated oldtask " + bundle.getString(MainActivity.BUNDLE_TASK_NAME).toString() + "to newtask" + editTextEditTask.getText().toString() +"!" , Toast.LENGTH_SHORT).show();
                    closeActivity();
                }
                else {
                    editTextEditTask.setText(bundle.getString(MainActivity.BUNDLE_TASK_NAME));
                    editTextEditTask.setSelection(editTextEditTask.getText().length());
                    Toast.makeText(getApplicationContext(), "task already exists, choose a different name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    public void closeActivity(){
        Intent resultIntent = new Intent();
        if (this.checkEditOnTask){
            setResult(MainActivity.RESULT_OK, resultIntent);
        }
        else{
            setResult(MainActivity.RESULT_CANCELED, resultIntent);
        }
        finish();
    }
}
