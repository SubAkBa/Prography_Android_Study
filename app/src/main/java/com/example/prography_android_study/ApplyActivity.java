package com.example.prography_android_study;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ApplyActivity extends AppCompatActivity {
    Button checkidbtn, completebtn, cancelbtn;
    EditText userid, userpw, userpw2, username, usermail;
    Spinner maillist;
    String[] strlist;

    UserDB db;
    UserDAO userdao;

    ArrayList<User> user;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        db = UserDB.getDatabase(getApplicationContext());
        userdao = db.userDao();

        checkidbtn = (Button) findViewById(R.id.checkidbtn);
        completebtn = (Button) findViewById(R.id.completebtn);
        cancelbtn = (Button) findViewById(R.id.cancelbtn);

        userid = (EditText) findViewById(R.id.userid);
        userpw = (EditText) findViewById(R.id.userpw);
        userpw2 = (EditText) findViewById(R.id.userpw2);
        username = (EditText) findViewById(R.id.username);
        usermail = (EditText) findViewById(R.id.usermail);

        maillist = (Spinner) findViewById(R.id.maillist);

        strlist = getResources().getStringArray(R.array.maillist);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.maillistlayout, strlist);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        RetrofitConnect retrofitConnection = new RetrofitConnect();
        Call<Get_My_Data> call =  retrofitConnection.server.get_data();
        call.enqueue(new Callback<Get_My_Data>() {
            @Override
            public void onResponse(Call<Get_My_Data> call, Response<Get_My_Data> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 서버에서 데이터 불러옴.
                }else {
                    // 서버와 연결은 되었으나, 오류 발생
                }
            }
        };



        maillist.setAdapter(adapter);
        maillist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ApplyActivity.this);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.setMessage("메일 종류를 선택해주세요 !");
                alert.show();
            }
        });

        checkidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ApplyActivity.this);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        count = userdao.CheckUserId(userid.getText().toString());
                    }
                });

                thread.start();

                try {
                    thread.join();
                } catch(Exception e){
                    e.getStackTrace();
                }

                if (userid.getText().toString().replaceAll(" ", "").equals(""))
                    alert.setMessage("ID를 입력해주세요 !");
                else if (count == 1)
                    alert.setMessage("중복된 ID 입니다.");
                else {
                    alert.setMessage("사용 가능한 ID 입니다.");

                    userid.setEnabled(false);
                    checkidbtn.setEnabled(false);
                }

                alert.show();
            }
        });

        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ApplyActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                if (userpw.getText().toString().replaceAll(" ", "").equals("") ||
                        userpw2.getText().toString().replaceAll(" ", "").equals("") ||
                        username.getText().toString().replaceAll(" ", "").equals("") ||
                        usermail.getText().toString().replaceAll(" ", "").equals("")) {
                    alert.setMessage("빈칸을 채워주세요 !");
                    alert.show();

                    return;
                }

                if (checkidbtn.isEnabled()) {
                    alert.setMessage("ID 중복확인을 해주세요 !");
                    alert.show();

                    return;
                }

                if (!userpw.getText().toString().equals(userpw2.getText().toString())) {
                    alert.setMessage("PW를 다시 한 번 확인해주세요 ! ");
                    alert.show();

                    return;
                }

                if (userpw.getText().toString().length() < 8) {
                    alert.setMessage("PW는 8글자 이상이여야 해요 !");
                    alert.show();

                    return;
                }


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userdao.InsertUser(new User(userid.getText().toString(), userpw.getText().toString(),
                                username.getText().toString(), usermail.getText().toString()));
                    }
                });

                thread.start();

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                alert.setMessage("회원가입이 완료되었습니다.");
                alert.show();

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertbuilders = new AlertDialog.Builder(ApplyActivity.this);

                alertbuilders.setMessage("회원가입을 종료할까요 ?").setCancelable(false);

                alertbuilders.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });

                AlertDialog alert = alertbuilders.create();
                alert.show();
            }
        });
    }

    @Override
    public void onFailure(Call<Get_My_Data> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.toString()); //서버와 연결 실패
    }
}
