package com.example.prography_android_study;

import android.content.*;

import androidx.room.*;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDB extends RoomDatabase {

    public abstract UserDAO userDao();

    private static volatile UserDB INSTANCE = null;

    public static UserDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDB.class, "UserDB").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}