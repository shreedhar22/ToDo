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
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    SimpleDateFormat dateFormatter;
    DBHandle db;
    EditText editTextTaskName;
    EditText editTextTaskNotes;
    EditText editTextTaskDueDate;
    Spinner spinnerTaskStatus;
    Spinner spinnerTaskPriority;
    Boolean checkAddOnTask = Boolean.FALSE;

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

        spinnerTaskPriority = (Spinner) findViewById(R.id.spinnerTaskPriority);
        spinnerTaskPriority.setOnItemSelectedListener(new CustomSpinnerOnItemSelectedListener());

        spinnerTaskStatus = (Spinner) findViewById(R.id.spinnerTaskStatus);
        spinnerTaskStatus.setOnItemSelectedListener(new CustomSpinnerOnItemSelectedListener());



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
