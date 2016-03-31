package com.neelkrishna.todotaskorganizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.neelkrishna.todotaskorganizer.database.ToDoItemBaseHelper;
import com.neelkrishna.todotaskorganizer.database.ToDoItemCursorWrapper;
import com.neelkrishna.todotaskorganizer.database.ToDoItemDBSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by neelkrishna
 */
public class Singleton {
    public static Singleton staticSingleton;
    public Context mContext;
    private SQLiteDatabase mDatabase;

    public static Singleton get(Context context) {
        if (staticSingleton == null) {
            staticSingleton = new Singleton(context);
        }
        return staticSingleton;
    }

    private Singleton(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ToDoItemBaseHelper(mContext).getWritableDatabase();
    }

    public void addToDoItem(ToDoItem toDoItem) {
        ContentValues values = getContentValues(toDoItem);

        mDatabase.insert(ToDoItemDBSchema.ToDoItemTable.NAME, null, values);
    }

    public List<ToDoItem> getToDoItems() {
        List<ToDoItem> toDoItems = new ArrayList<>();

        ToDoItemCursorWrapper cursor = queryToDoItems(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            toDoItems.add(cursor.getToDoItem());
            cursor.moveToNext();
        }
        cursor.close();

        return toDoItems;
    }

    public ToDoItem getToDoItem(UUID id) {
        ToDoItemCursorWrapper cursor = queryToDoItems(
                ToDoItemDBSchema.ToDoItemTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getToDoItem();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(ToDoItem toDoItem) {
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, toDoItem.getPhotoFilename());
    }

    public void updateToDoItem(ToDoItem toDoItem) {
        String uuidString = toDoItem.getId().toString();
        ContentValues values = getContentValues(toDoItem);

        mDatabase.update(ToDoItemDBSchema.ToDoItemTable.NAME, values,
                ToDoItemDBSchema.ToDoItemTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(ToDoItem toDoItem) {
        ContentValues values = new ContentValues();
        values.put(ToDoItemDBSchema.ToDoItemTable.Cols.UUID, toDoItem.getId().toString());
        values.put(ToDoItemDBSchema.ToDoItemTable.Cols.TITLE, toDoItem.getTitle());
        values.put(ToDoItemDBSchema.ToDoItemTable.Cols.DUE_DATE, toDoItem.getDueDate().getTime());
        values.put(ToDoItemDBSchema.ToDoItemTable.Cols.COMPLETED, toDoItem.isCompleted() ? 1 : 0);

        return values;
    }

    private ToDoItemCursorWrapper queryToDoItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ToDoItemDBSchema.ToDoItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new ToDoItemCursorWrapper(cursor);
    }
}
