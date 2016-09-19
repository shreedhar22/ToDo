package com.sudhishkr.codepath.todo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_ViewTaskActivity = 0;
    static final int REQUEST_CODE_AddTaskActivity = 1;
    static final int REQUEST_CODE_EditTaskActivity = 2;

    static final String BUNDLE_TASK_NAME = "TASK_NAME";
    static final String BUNDLE_TASK_NOTES = "TASK_NOTES";
    static final String BUNDLE_TASK_PRIORITY = "TASK_PRIORITY";
    static final String BUNDLE_TASK_DUE_DATE = "TASK_DUE_DATE";
    static final String BUNDLE_TASK_STATUS = "TASK_STATUS";

    ListView listViewTask;
    DBHandle db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
        }

        db = new DBHandle(this);

        listViewTask = (ListView) findViewById(R.id.listViewShowTask);
        populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  itemValue = (String) listViewTask.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "view mode for task " +itemValue+ "!" , Toast.LENGTH_SHORT).show();
                Intent viewTaskIntent = new Intent(MainActivity.this, ViewTaskActivity.class);
                viewTaskIntent.putExtra(BUNDLE_TASK_NAME, itemValue);
                viewTaskIntent.putExtra(BUNDLE_TASK_NOTES, db.getTaskNotes(itemValue));
                viewTaskIntent.putExtra(BUNDLE_TASK_DUE_DATE, db.getTaskDueDate(itemValue));
                viewTaskIntent.putExtra(BUNDLE_TASK_PRIORITY, db.getTaskPriority(itemValue));
                viewTaskIntent.putExtra(BUNDLE_TASK_STATUS, db.getTaskStatus(itemValue));
                MainActivity.this.startActivityForResult(viewTaskIntent, MainActivity.REQUEST_CODE_EditTaskActivity);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_appbar_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_addTask:
                Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                MainActivity.this.startActivityForResult(addTaskIntent, MainActivity.REQUEST_CODE_AddTaskActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (MainActivity.REQUEST_CODE_AddTaskActivity) :
                if (resultCode == MainActivity.RESULT_OK) {
                    populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
                }
                break;
            case (MainActivity.REQUEST_CODE_ViewTaskActivity) :
                if (resultCode == MainActivity.RESULT_OK) {
                    populateListView(listViewTask, db.getAllTasks(), getApplicationContext());
                }
                break;
        }
    }

    public static void populateListView(ListView listViewTask, String[] values, Context context){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listViewTask.setAdapter(adapter);
    }
}