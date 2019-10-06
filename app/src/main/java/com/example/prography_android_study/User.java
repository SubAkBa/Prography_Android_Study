package com.example.prography_android_study;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userid")
    public String userid;

    public String userpw;
    public String username;
    public String usermail;

    public User(String userid, String userpw, String username, String usermail) {
        this.userid = userid;
        this.userpw = userpw;
        this.username = username;
        this.usermail = usermail;
    }

    public String getUserid() { return userid; }

    public String getUserpw() { return userpw; }

    public String getUsername() {
        return username;
    }

    public String getUsermail() {
        return usermail;
    }
}
