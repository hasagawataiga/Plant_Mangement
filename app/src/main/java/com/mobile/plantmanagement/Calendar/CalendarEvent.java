package com.mobile.plantmanagement.Calendar;

public class CalendarEvent {
    private String title;
    private String content;

    public CalendarEvent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "name='" + title + '\'' +
                ", amount='" + content + '\'' +
                '}';
    }
}
