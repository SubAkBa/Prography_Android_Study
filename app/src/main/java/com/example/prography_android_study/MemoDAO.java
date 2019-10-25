package com.example.prography_android_study;

import androidx.room.*;
import java.util.*;

@Dao
public interface MemoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertMemo(Memo... memos);

    @Query("DELETE FROM Memo WHERE userid = :userid")
    public void DeleteAllMemo(String userid);

    @Query("UPDATE memo SET title = :title, content = :content, date = :date, time = :time WHERE memoidx = :pos AND userid = :userid")
    public void UpdateMemoInfo(String title, String content, String date, String time, String pos, String userid);

    @Query("DELETE FROM Memo WHERE memoidx = :idx AND userid = :userid")
    public void DeleteMemoInfo(String idx, String userid);

    @Query("SELECT COUNT(*) FROM Memo WHERE userid = :inputuserid")
    public int GetMemosCount(String inputuserid);

    @Query("SELECT * FROM Memo WHERE userid = :inputuserid")
    public List<Memo> GetUserMemos(String inputuserid);

}
