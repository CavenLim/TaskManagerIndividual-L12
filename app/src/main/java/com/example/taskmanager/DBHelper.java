package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_Task = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK_TITLE = "task_title";
    private static final String COLUMN_TASK_DESC = "task_desc";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNoteTableSql = "CREATE TABLE " + TABLE_Task + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_TITLE + " TEXT,"+ COLUMN_TASK_DESC + " TEXT )";
        db.execSQL(createNoteTableSql);
        Log.i("info", "created tables");

        for (int i = 0; i< 4; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID,i);
            values.put(COLUMN_TASK_TITLE, "TEST Tasks " + i);
            values.put(COLUMN_TASK_DESC, "TEST DESC " + i);
            db.insert(TABLE_Task, null, values);
        }
        Log.i("info", "dummy records inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + TABLE_Task + " ADD COLUMN module_name TEXT ");
    }

    public long insertTask(String taskTitle,String taskDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, taskTitle);
        values.put(COLUMN_TASK_DESC, taskDesc);
        long result = db.insert(TABLE_Task, null, values);
        db.close();
        if (result == -1){
            Log.d("DBHelper","Insert failed");
        }
        else{
            Log.d("SQL Insert","ID:"+ result);
        }

        //id returned, shouldn’t be -1
        return result;
    }


    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();

        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_TASK_TITLE + ","  + COLUMN_TASK_DESC + " FROM " + TABLE_Task;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String taskTitle = cursor.getString(1);
                String taskDesc = cursor.getString(2);
                Task task = new Task(id, taskTitle,taskDesc);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }
  

}
