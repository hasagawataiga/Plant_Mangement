package com.mobile.plantmanagement;

import android.util.Log;

public class CalendarNotes {
    private String name;
    private String content;
    private final String TAG = "CALENDAR_NOTES";
    public CalendarNotes(Object content){
        if (content instanceof String) {
            this.content = (String) content;
        } else {
            Log.d(TAG, "Content is not of type String");
        }
    }
    public CalendarNotes(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CalendarNotes{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
