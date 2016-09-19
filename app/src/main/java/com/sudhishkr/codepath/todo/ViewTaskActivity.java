package com.sudhishkr.codepath.todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTaskActivity extends AppCompatActivity {

    DBHandle db;
    Bundle bundle;

    EditText editTextTaskName;
    EditText editTextTaskNotes;
    EditText editTextTaskDueDate;
    Spinner spinnerTaskStatus;
    Spinner spinnerTaskPriority;

    Boolean checkDeleteOnTask = Boolean.FALSE;
    Boolean checkEditOnTask = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
        }

        db = new DBHandle(this);

        bundle = getIntent().getExtras();

        editTextTaskName = (EditText) findViewById(R.id.editTextTaskName);
        editTextTaskName.setText(bundle.getString(MainActivity.BUNDLE_TASK_NAME));
        editTextTaskName.setCursorVisible(false);
        editTextTaskName.setKeyListener(null);
        editTextTaskName.setFocusable(false);
        editTextTaskName.setEnabled(false);

        editTextTaskNotes = (EditText) findViewById(R.id.editTextTaskNotes);
        editTextTaskNotes.setText(db.getTaskNotes(bundle.getString(MainActivity.BUNDLE_TASK_NAME)));
        editTextTaskNotes.setCursorVisible(false);
        editTextTaskNotes.setKeyListener(null);
        editTextTaskNotes.setFocusable(false);
        editTextTaskNotes.setEnabled(false);

        editTextTaskDueDate = (EditText) findViewById(R.id.editTextTaskDueDate);
        editTextTaskDueDate.setText(db.getTaskDueDate(bundle.getString(MainActivity.BUNDLE_TASK_NAME)));
        editTextTaskDueDate.setCursorVisible(false);
        editTextTaskDueDate.setKeyListener(null);
        editTextTaskDueDate.setFocusable(false);
        editTextTaskDueDate.setEnabled(false);

        spinnerTaskPriority = (Spinner) findViewById(R.id.spinnerTaskPriority);
        //spinnerTaskPriority.setSelection(bundle.getInt(MainActivity.BUNDLE_TASK_PRIORITY));

        spinnerTaskStatus = (Spinner) findViewById(R.id.spinnerTaskStatus);
        //spinnerTaskPriority.setSelection(bundle.getInt(MainActivity.BUNDLE_TASK_STATUS));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_appbar_view_task, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                closeActivity();
                return true;
            case R.id.appbar_editTask:
                Intent editTaskIntent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
                editTaskIntent.putExtra(MainActivity.BUNDLE_TASK_NAME, editTextTaskName.getText().toString());
                ViewTaskActivity.this.startActivityForResult(editTaskIntent, MainActivity.REQUEST_CODE_EditTaskActivity);
                //closeActivity();
                return true;
            case R.id.appbar_deleteTask:
                checkDeleteOnTask = Boolean.TRUE;
                db.deleteTask(editTextTaskName.getText().toString());
                closeActivity();
                return true;
            case R.id.appbar_closeTask:
                closeActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (MainActivity.REQUEST_CODE_EditTaskActivity) :
                if (resultCode == ViewTaskActivity.RESULT_OK) {
                    checkEditOnTask = Boolean.TRUE;
                    closeActivity();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    public void closeActivity(){
        Intent resultIntent = new Intent();
        if (this.checkDeleteOnTask || this.checkEditOnTask){
            setResult(MainActivity.RESULT_OK, resultIntent);
        }
        else{
            setResult(MainActivity.RESULT_CANCELED, resultIntent);
        }
        finish();
    }
}
