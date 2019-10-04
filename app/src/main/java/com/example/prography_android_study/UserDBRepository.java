package com.example.prography_android_study;

import android.app.*;
import android.os.*;

public class UserDBRepository {

    private UserDAO userdao;

    UserDBRepository(Application app){
        UserDB db = UserDB.getDatabase(app);
        userdao = db.userDao();
    }

    public void InsertUser(User user){
        new insertAsyncTask(userdao).execute(user);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        insertAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.InsertUser(params[0]);
            return null;
        }
    }
}
