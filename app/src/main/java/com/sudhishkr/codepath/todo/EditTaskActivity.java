package com.sudhishkr.codepath.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    DBHandle db;
    Bundle bundle;

    EditText editTextTaskName;
    EditText editTextTaskNotes;
    EditText editTextTaskDueDate;
    Spinner spinnerTaskStatus;
    Spinner spinnerTaskPriority;

    Boolean checkEditOnTask = Boolean.FALSE;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 0;
    DatePickerDialog.OnDateSetListener dateSetListener;

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
        editTextTaskName.setSelection(editTextTaskName.getText().length());

        editTextTaskNotes = (EditText) findViewById(R.id.editTextTaskNotes);
        editTextTaskNotes.setText(db.getTaskNotes(bundle.getString(MainActivity.BUNDLE_TASK_NAME)));

        editTextTaskDueDate = (EditText) findViewById(R.id.editTextTaskDueDate);
        editTextTaskDueDate.setText(db.getTaskDueDate(bundle.getString(MainActivity.BUNDLE_TASK_NAME)));
        editTextTaskDueDate.setCursorVisible(false);
        editTextTaskDueDate.setKeyListener(null);
        editTextTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        spinnerTaskPriority = (Spinner) findViewById(R.id.spinnerTaskPriority);
        spinnerTaskPriority.setSelection(Arrays.asList(getResources().getStringArray(R.array.priority_arrays)).indexOf(db.getTaskPriority(bundle.getString(MainActivity.BUNDLE_TASK_NAME))));

        spinnerTaskStatus = (Spinner) findViewById(R.id.spinnerTaskStatus);
        spinnerTaskStatus.setSelection(Arrays.asList(getResources().getStringArray(R.array.status_arrays)).indexOf(db.getTaskStatus(bundle.getString(MainActivity.BUNDLE_TASK_NAME))));

        year = Integer.valueOf(db.getTaskDueDate(bundle.getString(MainActivity.BUNDLE_TASK_NAME)).split("-")[2]);
        month = Integer.valueOf(db.getTaskDueDate(bundle.getString(MainActivity.BUNDLE_TASK_NAME)).split("-")[1]);
        day = Integer.valueOf(db.getTaskDueDate(bundle.getString(MainActivity.BUNDLE_TASK_NAME)).split("-")[0]);

        updateDisplay();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EditTaskActivity.this.year = year;
                month = monthOfYear;
                day = dayOfMonth;
                updateDisplay();
            }
        };

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        dateSetListener,
                        year, month, day);
        }
        return null;
    }

    private void updateDisplay() {
        this.editTextTaskDueDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(month + 1).append("-")
                        .append(day).append("-")
                        .append(year));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_appbar_add_task, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                closeActivity();
                return true;
            case R.id.appbar_saveTask:
                db.updateTask(bundle.getString(MainActivity.BUNDLE_TASK_NAME), editTextTaskName.getText().toString(), editTextTaskNotes.getText().toString(), spinnerTaskPriority.getSelectedItem().toString(), editTextTaskDueDate.getText().toString(), spinnerTaskStatus.getSelectedItem().toString());
                checkEditOnTask = Boolean.TRUE;
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
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    public void closeActivity(){
        Intent resultIntent = new Intent();
        if (this.checkEditOnTask){
            setResult(ViewTaskActivity.RESULT_OK, resultIntent);
        }
        else{
            setResult(ViewTaskActivity.RESULT_CANCELED, resultIntent);
        }
        this.finish();
    }
}
