package com.example.prography_android_study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    EditText idtext, pwtext;
    Button loginbtn, applybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, ApplyActivity.class);

        idtext = (EditText)findViewById(R.id.idspace);
        pwtext = (EditText)findViewById(R.id.pwspace);

        loginbtn = (Button)findViewById(R.id.login);
        applybtn = (Button)findViewById(R.id.apply);

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

        applybtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(intent);
            }
        });
    }
}
