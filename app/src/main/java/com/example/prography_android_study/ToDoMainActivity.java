package com.example.prography_android_study;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ToDoMainActivity extends AppCompatActivity {

    Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);

        logoutbtn = (Button)findViewById(R.id.logoutbtn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spclear = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = spclear.edit();

                edit.clear();
                edit.commit();

                Toast.makeText(ToDoMainActivity.this, "로그아웃 합니다.", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }
}
