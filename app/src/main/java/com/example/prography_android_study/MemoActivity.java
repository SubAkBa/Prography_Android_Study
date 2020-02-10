package com.example.prography_android_study;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {
    long now;
    String[] getDate, date, time;
    String position, userid, text;

    SimpleDateFormat sdf;

    Button datebtn, timebtn, addbtn;
    EditText title, content;

    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;

    MemoDB db;
    MemoDAO memodao;
    ActionBar actionbar;

    ArrayList<Memo> memo;

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);

        return builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memo_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.etcbtn) {
            AlertDialog.Builder alertbuilder = new AlertDialog.Builder(MemoActivity.this);

            alertbuilder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    memodao.DeleteMemoInfo(position, userid);
                                }
                            });

                            thread.start();

                            try {
                                thread.join();
                            } catch (Exception e) {
                                e.getStackTrace();
                            }

                            Intent intent = new Intent();
                            intent.putExtra("del", "1");
                            intent.putExtra("pos", position);
                            setResult(RESULT_OK, intent);

                            Toast.makeText(getApplicationContext(), "메모가 삭제되었어요.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });

            AlertDialog alert = alertbuilder.create();

            alert.setMessage("메모를 삭제할까요?");
            alert.show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        actionbar = getActionBar();

        db = MemoDB.getDatabase(getApplicationContext());
        memodao = db.memoDao();

        datebtn = (Button) findViewById(R.id.datebtn);
        timebtn = (Button) findViewById(R.id.timebtn);
        addbtn = (Button) findViewById(R.id.addbtn);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);

        Intent info = getIntent();
        userid = info.getStringExtra("userid");

        if (!info.getStringExtra("title").equals("")) {
            title.setText(info.getStringExtra("title"));
            content.setText(info.getStringExtra("content"));
            datebtn.setText(info.getStringExtra("date").replaceAll("/", "     /     "));
            timebtn.setText(info.getStringExtra("time").replaceAll(":", "     :     "));
            position = info.getStringExtra("pos");

            date = info.getStringExtra("date").split("/");
            time = info.getStringExtra("time").replaceAll(" ", "").split(":");
            addbtn.setText("메모 변경");

        } else {
            now = System.currentTimeMillis();

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            getDate = sdf.format(new Date(now)).split(" ");

            position = "-1";
            date = getDate[0].split("-");
            time = getDate[1].split(":");

            datebtn.setText(getDate[0].replaceAll("-", "     /     "));
            timebtn.setText(getDate[1].replaceAll(":", "     :     "));
            addbtn.setText("메모 추가");
        }

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

                if (addbtn.getText().toString().equals("메모 추가")) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            position = Integer.toString(memodao.GetMemosCount(userid));
                            memodao.InsertMemo(new Memo(
                                    position,
                                    userid,
                                    title.getText().toString(),
                                    content.getText().toString(),
                                    datebtn.getText().toString(),
                                    timebtn.getText().toString()));
                        }
                    });

                    thread.start();

                    try {
                        thread.join();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                    text = "추가";
                } else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            memodao.UpdateMemoInfo(
                                    title.getText().toString(),
                                    content.getText().toString(),
                                    datebtn.getText().toString(),
                                    timebtn.getText().toString(),
                                    position,
                                    userid);
                        }
                    });

                    thread.start();

                    try {
                        thread.join();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                    text = "변경";
                    result.putExtra("del", "0");
                }

                result.putExtra("pos", position);

                if (title.getText().toString().equals(""))
                    result.putExtra("title", "제목없음");
                else
                    result.putExtra("title", title.getText().toString());

                result.putExtra("content", content.getText().toString());
                result.putExtra("date", datebtn.getText().toString());
                result.putExtra("time", timebtn.getText().toString());

                Toast.makeText(getApplicationContext(), "메모 " + text + " 이(가) 완료되었어요.", Toast.LENGTH_LONG).show();

                setResult(RESULT_OK, result);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(date[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(date[1]));
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                cal.set(Calendar.SECOND, 0);

                scheduleNotification(getNotification(title.getText().toString()), cal.getTimeInMillis());

                finish();
            }
        });
    }

}
