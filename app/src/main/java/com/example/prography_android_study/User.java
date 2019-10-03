package com.example.prography_android_study;

import androidx.annotation.*;
import androidx.room.*;

import org.jetbrains.annotations.NonNls;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String userid;

    public String userpw;
    public String username;
    public String usermail;

    public User(String userid, String userpw, String username, String usermail){
        this.userid = userid;
        this.userpw = userpw;
        this.username = username;
        this.usermail = usermail;

    }

}
