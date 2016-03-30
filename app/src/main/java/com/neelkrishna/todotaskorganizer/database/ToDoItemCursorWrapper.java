package com.neelkrishna.todotaskorganizer.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.neelkrishna.todotaskorganizer.ToDoItem;

import java.util.Date;
import java.util.UUID;

/**
 * Created by neelkrishna
 */
public class ToDoItemCursorWrapper extends CursorWrapper{
    public ToDoItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ToDoItem getToDoItem() {
        String uuidString = getString(getColumnIndex(ToDoItemDBSchema.ToDoItemTable.Cols.UUID));
        String title = getString(getColumnIndex(ToDoItemDBSchema.ToDoItemTable.Cols.TITLE));
        long dueDate = getLong(getColumnIndex(ToDoItemDBSchema.ToDoItemTable.Cols.DUE_DATE));
        int isCompleted = getInt(getColumnIndex(ToDoItemDBSchema.ToDoItemTable.Cols.COMPLETED));

        ToDoItem toDoItem = new ToDoItem(UUID.fromString(uuidString));
        toDoItem.setTitle(title);
        toDoItem.setDueDate(new Date(dueDate));
        toDoItem.setCompleted(isCompleted != 0);

        return toDoItem;
    }
}
