package com.neelkrishna.todotaskorganizer.database;

/**
 * Created by neelkrishna
 */
public class ToDoItemDBSchema {
    public static final class ToDoItemTable {
        public static final String NAME = "toDoItems";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DUE_DATE = "due_date";
            public static final String COMPLETED = "completed";
        }
    }
}
