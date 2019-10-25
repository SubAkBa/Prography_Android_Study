package com.example.prography_android_study;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "Memo", primaryKeys = {"memoidx", "userid"})

public class Memo {
    @NonNull
    public String memoidx;
    @NonNull
    public String userid;
    public String title;
    public String content;
    public String date;
    public String time;

    public Memo(String memoidx, String userid, String title, String content, String date, String time) {
        this.memoidx = memoidx; this.userid = userid;
        this.title = title; this.content = content;
        this.date = date; this.time = time;
    }

    public void setIndex(@NonNull String memoidx) { this.memoidx = memoidx; }
    public void setUserid(String userid) { this.userid = userid; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }

    @NonNull
    public String getIdx() { return memoidx; }

    public String getUserid() { return userid; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}