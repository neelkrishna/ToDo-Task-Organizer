package com.neelkrishna.todotaskorganizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.neelkrishna.todotaskorganizer.ToDoItem;

/**
 * Created by neelkrishna
 */
public class ToDoItemBaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "ToDoItemBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "toDoItemBase.db";

    public ToDoItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + ToDoItemDBSchema.ToDoItemTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        ToDoItemDBSchema.ToDoItemTable.Cols.UUID + ", " +
                        ToDoItemDBSchema.ToDoItemTable.Cols.TITLE + ", " +
                        ToDoItemDBSchema.ToDoItemTable.Cols.DUE_DATE + ", " +
                        ToDoItemDBSchema.ToDoItemTable.Cols.COMPLETED +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
