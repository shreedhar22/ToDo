package com.sudhishkr.codepath.todo;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHandle extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TASK_TABLE = "tasktable";
    public static final String TASK_TABLE_COLUMN_ID = "id";
    public static final String TASK_TABLE_COLUMN_TASK = "task";

    public DBHandle(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TASK_TABLE+" ("+TASK_TABLE_COLUMN_ID+" integer primary key, "+TASK_TABLE_COLUMN_TASK+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TASK_TABLE);
        onCreate(db);
    }

    public boolean insertTask(String task) {
        if (!checkTaskExists(task)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TASK_TABLE_COLUMN_TASK, task);
            db.insert(TASK_TABLE, null, contentValues);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkTaskExists(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TASK_TABLE +" where "+ TASK_TABLE_COLUMN_TASK +" = '"+task+"'", null );
        if (res.getCount() > 0)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public Integer getTaskId(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TASK_TABLE +" where "+ TASK_TABLE_COLUMN_TASK +" = '"+task+"'", null );
        res.moveToFirst();
        return res.getInt(res.getColumnIndex(TASK_TABLE_COLUMN_ID));
    }

    public Integer deleteTask (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASK_TABLE, TASK_TABLE_COLUMN_ID +" = ? ", new String[] { Integer.toString(getTaskId(task)) });
    }

    public boolean updateTask(String oldTask, String newTask){
        if (oldTask.equals(newTask)){
            return Boolean.FALSE;
        }
        else {
            if (!checkTaskExists(newTask)){
                deleteTask(oldTask);
                insertTask(newTask);
                return Boolean.TRUE;
            }
            else {
                return Boolean.FALSE;
            }
        }
    }

    public String[] getAllTasks()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TASK_TABLE, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TASK_TABLE_COLUMN_TASK)));
            res.moveToNext();
        }

        String[] tempArray = new String[array_list.size()];
        return array_list.toArray(tempArray);
    }
}
