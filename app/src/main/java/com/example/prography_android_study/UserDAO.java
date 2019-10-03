package com.example.prography_android_study;

import androidx.room.*;

import java.util.*;

@Dao
public interface UserDAO {
    @Query("SELECT userid FROM User WHERE userid == :inputuserid")
    public String CheckUserId(String inputuserid);

    @Insert
    public void Insert(User user);

    @Query("DELETE FROM User WHERE userid == :inputuserid")
    public void DeleteUserInfo(String inputuserid);

}
