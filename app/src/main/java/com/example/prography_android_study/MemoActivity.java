package com.example.prography_android_study;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoActivity extends AppCompatActivity {
    long now;
    String[] getDate, date, time;
    String position;

    SimpleDateFormat sdf;

    Button datebtn, timebtn, addbtn;
    EditText title, content;

    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        datebtn = (Button)findViewById(R.id.datebtn);
        timebtn = (Button)findViewById(R.id.timebtn);
        addbtn = (Button)findViewById(R.id.addbtn);

        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);

        now = System.currentTimeMillis();

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        getDate = sdf.format(new Date(now)).split(" ");

        position = "-1";
        date = getDate[0].split("-");
        time = getDate[1].split(":");

        datebtn.setText(getDate[0].replaceAll("-", "     /     "));
        timebtn.setText(getDate[1].replaceAll(":", "     :     "));

        datelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datebtn.setText(year + "     /     " + month + "     /     " + dayOfMonth);
            }
        };

        timelistener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timebtn.setText(hourOfDay + "     :     " + minute);
            }
        };

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(MemoActivity.this, datelistener,
                        Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

                dialog.show();
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(MemoActivity.this, timelistener,
                        Integer.parseInt(time[0]), Integer.parseInt(time[1]), true);

                dialog.show();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();

                if(title.getText().toString().equals(""))
                    result.putExtra("title", "제목없음");
                else
                    result.putExtra("title", title.getText().toString());

                result.putExtra("content", content.getText().toString());
                result.putExtra("date", datebtn.getText().toString());
                result.putExtra("time", timebtn.getText().toString());
                if(!position.equals("-1"))
                    result.putExtra("pos", position);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            if (requestCode == 3001) {
                title.setText(data.getStringExtra("title"));
                content.setText(data.getStringExtra("content"));
                datebtn.setText(data.getStringExtra("date"));
                timebtn.setText(data.getStringExtra("time"));
                position = data.getStringExtra("pos");
            }
        }
    }
}
