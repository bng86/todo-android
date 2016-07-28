package com.oursky.todo_android.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oursky.todo_android.content.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyauchun on 13/3/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";

    private static final String TABLE_TASK = "tasks";

    // Common column names
    private static final String KEY_ID = "id";

    // NOTES Table - column names
    private static final String KEY_TASK = "task";
    private static final String KEY_FINISHED = "finished";
    private static final String KEY_DUE_AT = "due_at";

    // Task table create statement
    private static final String CREATE_TABLE_TASK = "CREATE TABLE "
            + TABLE_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK
            + " TEXT," + KEY_FINISHED + " INTEGER," + KEY_DUE_AT
            + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        // create new tables
        onCreate(db);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                task.setName(c.getString(c.getColumnIndex(KEY_TASK)));
                task.setDueAt(c.getString(c.getColumnIndex(KEY_DUE_AT)));
                int isFinished = c.getInt(c.getColumnIndex(KEY_FINISHED));
                task.setIsFinished(isFinished == 0 ? false : true);

                tasks.add(task);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }

    public List<Task> getAllUnfinishedtasks() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK
                + " WHERE " + KEY_FINISHED + " = " + " 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                task.setName(c.getString(c.getColumnIndex(KEY_TASK)));
                task.setDueAt(c.getString(c.getColumnIndex(KEY_DUE_AT)));
                task.setIsFinished(false);

                tasks.add(task);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }

    public List<Task> getAllFinishedTasks() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK
                + " WHERE " + KEY_FINISHED + " = " + " 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                task.setName(c.getString(c.getColumnIndex(KEY_TASK)));
                task.setDueAt(c.getString(c.getColumnIndex(KEY_DUE_AT)));
                task.setIsFinished(true);

                tasks.add(task);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }

    public long createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK, task.getName());
        values.put(KEY_DUE_AT, task.getDueAt());
        values.put(KEY_FINISHED, task.isFinished() ? 1 : 0);

        // insert row
        long task_id = db.insert(TABLE_TASK, null, values);

        return task_id;
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK, task.getName());
        values.put(KEY_DUE_AT, task.getDueAt());
        values.put(KEY_FINISHED, task.isFinished() ? 1 : 0);

        // updating row
        return db.update(TABLE_TASK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }
}
