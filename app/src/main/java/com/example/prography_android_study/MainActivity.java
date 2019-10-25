package com.example.prography_android_study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText idtext, pwtext;
    Button loginbtn, applybtn;

    UserDB db;
    UserDAO dao;

    int count = 0;
    boolean isexisted = false;

    CheckBox autologin;

    List<User> userlist;

    SharedPreferences sf;

    String loginID, loginPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userlist = new ArrayList<>();

        db = UserDB.getDatabase(getApplicationContext());
        dao = db.userDao();

        autologin = (CheckBox) findViewById(R.id.autologin);

        idtext = (EditText) findViewById(R.id.idspace);
        pwtext = (EditText) findViewById(R.id.pwspace);

        loginbtn = (Button) findViewById(R.id.login);
        applybtn = (Button) findViewById(R.id.apply);

        sf = getSharedPreferences("auto", MODE_PRIVATE);

        loginID = sf.getString("autoID", "");
        loginPW = sf.getString("autoPW", "");

        idtext.setText(loginID);

        if (!loginID.equals("") && !loginPW .equals("")) {
            Toast.makeText(MainActivity.this, loginID + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();

            Intent loginit = new Intent(MainActivity.this, ToDoMainActivity.class);
            loginit.putExtra("userid", loginID);
            startActivity(loginit);
            finish();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (autologin.isChecked()) {
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

                    SharedPreferences.Editor autoLogin = auto.edit();

                    autoLogin.putString("autoID", idtext.getText().toString());
                    autoLogin.putString("autoPW", pwtext.getText().toString());

                    autoLogin.commit();
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userlist = dao.CheckLogin(idtext.getText().toString(), pwtext.getText().toString());
                    }
                });

                thread.start();

                try {
                    thread.join();
                } catch (Exception e) {
                    e.getStackTrace();
                }

                for (int i = 0; i < userlist.size(); i++) {
                    User temp = userlist.get(i);

                    if (temp.userid.equals(temp.userid) && temp.userpw.equals(temp.userpw))
                        isexisted = true;
                }

                if (!isexisted) {
                    alert.setMessage("가입되어 있지 않거나 ID 또는 PW가 틀렸습니다.");
                    alert.show();

                    return;
                }

                alert.setMessage("로그인 되었습니다.");

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent loginintent = new Intent(MainActivity.this, ToDoMainActivity.class);
                        loginintent.putExtra("userid", loginID);
                        startActivity(loginintent);

                        finish();
                    }
                });

                alert.show();
            }
        });


        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ApplyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sp = getSharedPreferences("auto", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String text = idtext.getText().toString();
        editor.putString("autoID", text);

        editor.commit();
    }
}
