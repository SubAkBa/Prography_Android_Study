package com.example.prography_android_study;

import androidx.room.*;


@Dao
public interface UserDAO {
    @Query("SELECT * FROM User WHERE userid == :inputuserid")
    public User CheckUserId(String inputuserid);

    @Insert
    public void InsertUser(User user);

    @Query("DELETE FROM User WHERE userid == :inputuserid")
    public void DeleteUserInfo(String inputuserid);

    @Query("SELECT * FROM User WHERE userid == :inputuserid AND userpw == :inputuserpw")
    public User CheckLogin(String inputuserid, String inputuserpw);

}
