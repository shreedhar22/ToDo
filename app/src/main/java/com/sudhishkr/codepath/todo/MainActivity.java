package com.sudhishkr.codepath.todo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_EditTaskActivity = 1;
    static final String BUNDLE_TASK_NAME = "TASK_NAME";

    ListView listViewTask;
    DBHandle db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandle(this);

        listViewTask = (ListView) findViewById(R.id.listViewShowTask);
        populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  itemValue = (String) listViewTask.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "edit mode for task " +itemValue+ "!" , Toast.LENGTH_SHORT).show();
                Intent editTaskIntent = new Intent(MainActivity.this, EditTaskActivity.class);
                editTaskIntent.putExtra(BUNDLE_TASK_NAME, itemValue);
                MainActivity.this.startActivityForResult(editTaskIntent, MainActivity.REQUEST_CODE_EditTaskActivity);
            }
        });
        listViewTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String  itemValue = (String) listViewTask.getItemAtPosition(position);
                db.deleteTask(itemValue);
                Toast.makeText(getApplicationContext(), "deleted task " + itemValue + "!" , Toast.LENGTH_SHORT).show();
                populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
                return true;
            }
        });

        final EditText editTextAddTask = (EditText)findViewById(R.id.editTextAddTask);

        Button buttonAddTask = (Button)findViewById(R.id.buttonAddTask);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextAddTask.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "task text NOT entered!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(db.insertTask(editTextAddTask.getText().toString())){
                        populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
                        Toast.makeText(getApplicationContext(), "added task " + editTextAddTask.getText() + "!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "task already exists, choose a different name!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (MainActivity.REQUEST_CODE_EditTaskActivity) : {
                if (resultCode == MainActivity.RESULT_OK) {
                    populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
                }
                break;
            }
        }
    }

    public static void populateListView(ListView listViewTask, String[] values, Context context){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listViewTask.setAdapter(adapter);
    }
}