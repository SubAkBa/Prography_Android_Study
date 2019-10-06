package com.example.prography_android_study;

import androidx.room.*;


@Dao
public interface UserDAO {

    @Query("SELECT COUNT(*) FROM User WHERE userid = :inputuserid")
    public int CheckUserId(String inputuserid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertUser(User... users);

    @Query("DELETE FROM User WHERE userid = :inputuserid")
    public void DeleteUserInfo(String inputuserid);

    @Query("SELECT COUNT(*) FROM User WHERE userid = :inputuserid AND userpw = :inputuserpw")
    public int CheckLogin(String inputuserid, String inputuserpw);

}
