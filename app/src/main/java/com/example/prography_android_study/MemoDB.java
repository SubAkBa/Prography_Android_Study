package com.example.prography_android_study;

import android.content.*;
import androidx.room.*;

@Database(entities = {Memo.class}, version = 1, exportSchema = false)
public abstract class MemoDB extends RoomDatabase {

    public abstract MemoDAO memoDao();

    private static volatile MemoDB INSTANCE = null;

    public static MemoDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MemoDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MemoDB.class, "MemoDB").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}