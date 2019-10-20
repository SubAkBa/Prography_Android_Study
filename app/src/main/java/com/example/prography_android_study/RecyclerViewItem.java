package com.example.prography_android_study;

public class RecyclerViewItem {
    private String title, content, date, time;

    public RecyclerViewItem(String title, String content, String date, String time){
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getContent() { return content; }
    public String getTime() { return time; }

    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setContent(String content) { this.content = content; }
    public void setTime(String time) { this.time = time; }
}
