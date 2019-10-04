package com.example.prography_android_study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText idtext, pwtext;
    Button loginbtn, applybtn;

    UserDB db;
    UserDAO dao;

    ArrayList<User> loginuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = UserDB.getDatabase(getApplicationContext());
        dao = db.userDao();
        loginuser = new ArrayList<>();

        final Intent intent = new Intent(this, ApplyActivity.class);
        final Intent loginintent = new Intent(this, ToDoMainActivity.class);

        idtext = (EditText) findViewById(R.id.idspace);
        pwtext = (EditText) findViewById(R.id.pwspace);

        loginbtn = (Button) findViewById(R.id.login);
        applybtn = (Button) findViewById(R.id.apply);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginuser.add(dao.CheckLogin(idtext.getText().toString(), pwtext.getText().toString()));
                    }
                });

                thread.start();

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                if (loginuser.size() == 0) {
                    alert.setMessage("가입되어 있지 않거나 ID 또는 PW가 틀렸습니다.");
                    alert.show();
                    return;
                }

                alert.setMessage("로그인 되었습니다.");
                alert.show();

                startActivity(loginintent);
            }
        });

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}
