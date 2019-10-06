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

    int count;

    SharedPreferences sf;

    String savedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = UserDB.getDatabase(getApplicationContext());
        dao = db.userDao();

        final Intent intent = new Intent(this, ApplyActivity.class);
        final Intent loginintent = new Intent(this, ToDoMainActivity.class);

        idtext = (EditText) findViewById(R.id.idspace);
        pwtext = (EditText) findViewById(R.id.pwspace);

        loginbtn = (Button) findViewById(R.id.login);
        applybtn = (Button) findViewById(R.id.apply);

        sf = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        savedID = sf.getString("ID", "");
        idtext.setText(savedID);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        count = dao.CheckLogin(idtext.getText().toString(), pwtext.getText().toString());
                    }
                });

                thread.start();

                if (count == 0) {
                    alert.setMessage("가입되어 있지 않거나 ID 또는 PW가 틀렸습니다.");
                    alert.show();
                } else {
                    alert.setMessage("로그인 되었습니다.");

                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(loginintent);
                        }
                    });

                    alert.show();
                }
            }
        });

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String text = idtext.getText().toString();
        editor.putString("ID", text);

        editor.commit();
    }
}
