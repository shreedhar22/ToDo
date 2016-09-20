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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    SimpleDateFormat dateFormatter;
    DBHandle db;
    EditText editTextTaskName;
    EditText editTextTaskNotes;
    EditText editTextTaskDueDate;
    Spinner spinnerTaskStatus;
    Spinner spinnerTaskPriority;

    Boolean checkAddOnTask = Boolean.FALSE;

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

        editTextTaskName = (EditText) findViewById(R.id.editTextTaskName);
        editTextTaskNotes = (EditText) findViewById(R.id.editTextTaskNotes);
        editTextTaskDueDate = (EditText) findViewById(R.id.editTextTaskDueDate);
        editTextTaskDueDate.setCursorVisible(false);
        editTextTaskDueDate.setKeyListener(null);
        editTextTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        spinnerTaskPriority = (Spinner) findViewById(R.id.spinnerTaskPriority);
        spinnerTaskPriority.setOnItemSelectedListener(new CustomSpinnerOnItemSelectedListener());

        spinnerTaskStatus = (Spinner) findViewById(R.id.spinnerTaskStatus);
        spinnerTaskStatus.setOnItemSelectedListener(new CustomSpinnerOnItemSelectedListener());

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        AddTaskActivity.this.year = year;
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
                if (editTextTaskName.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "task text NOT entered!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(db.insertTask(editTextTaskName.getText().toString(), editTextTaskNotes.getText().toString(), spinnerTaskPriority.getSelectedItem().toString(), editTextTaskDueDate.getText().toString(), spinnerTaskStatus.getSelectedItem().toString())){
                        checkAddOnTask = Boolean.TRUE;
                        Toast.makeText(getApplicationContext(), "added task " + editTextTaskName.getText() + "!", Toast.LENGTH_SHORT).show();
                        closeActivity();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "task already exists, choose a different name!", Toast.LENGTH_SHORT).show();
                    }
                }
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
        if (this.checkAddOnTask){
            setResult(MainActivity.RESULT_OK, resultIntent);
        }
        else{
            setResult(MainActivity.RESULT_CANCELED, resultIntent);
        }
        finish();
    }
}
