package com.example.prography_android_study;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ToDoMainActivity extends AppCompatActivity {

    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);

        actionbar = getActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.writebtn){
            Intent write = new Intent(ToDoMainActivity.this, MemoActivity.class);

            startActivityForResult(write, 3000);
        }

        if(id == R.id.searchbtn){
            Toast.makeText(this, "검색 버튼", Toast.LENGTH_SHORT).show();

            return true;
        }

        if(id == R.id.etcbtn){
            SharedPreferences spclear = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = spclear.edit();

            edit.clear();
            edit.commit();

            Toast.makeText(ToDoMainActivity.this, "로그아웃 합니다.", Toast.LENGTH_LONG).show();

            Intent loginintent = new Intent(ToDoMainActivity.this, MainActivity.class);
            startActivity(loginintent);

            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            switch(requestCode){
                case 3000:
                    Toast.makeText(getApplicationContext(), data.getStringExtra("title"), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
