package com.neelkrishna.todotaskorganizer;

import java.util.Date;
import java.util.UUID;

/**
 * Created by neelkrishna
 */
public class ToDoItem {
    private UUID mId;
    private String mTitle = "";
    private Date mDueDate = new Date();
    private boolean mCompleted;

    public ToDoItem() {
        this(UUID.randomUUID());
    }

    public ToDoItem(UUID id) {
        mId = id;
        mDueDate = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date date) {
        mDueDate = date;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
