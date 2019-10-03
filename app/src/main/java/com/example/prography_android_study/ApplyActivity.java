package com.example.prography_android_study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class ApplyActivity extends AppCompatActivity {
    Button checkidbtn, completebtn, cancelbtn;
    EditText userid, userpw, userpw2, username, usermail;
    Spinner maillist;
    String[] strlist;

    UserDB db;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        db = UserDB.getDatabase(getApplicationContext());
        userdao = db.userDao();

        checkidbtn = (Button)findViewById(R.id.checkidbtn);
        completebtn = (Button)findViewById(R.id.completebtn);
        cancelbtn = (Button)findViewById(R.id.cancelbtn);

        userid = (EditText)findViewById(R.id.userid);
        userpw = (EditText)findViewById(R.id.userpw);
        userpw2 = (EditText)findViewById(R.id.userpw2);
        username = (EditText)findViewById(R.id.username);
        usermail = (EditText)findViewById(R.id.usermail);

        maillist = (Spinner)findViewById(R.id.maillist);

        strlist = getResources().getStringArray(R.array.maillist);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.maillistlayout, strlist);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

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

                if(userid.getText().toString().replaceAll(" ", "").equals(""))
                    alert.setMessage("ID를 입력해주세요 !");
                else if (!userdao.CheckUserId(userid.getText().toString()).equals(null))
                    alert.setMessage("중복된 ID 입니다.");
                else {
                    alert.setMessage("사용 가능한 ID 입니다.");
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

                if(userpw.getText().toString().replaceAll(" ", "").equals("") ||
                        userpw2.getText().toString().replaceAll(" ", "").equals("") ||
                        username.getText().toString().replaceAll(" ", "").equals("") ||
                        usermail.getText().toString().replaceAll(" ", "").equals("")){
                    alert.setMessage("빈칸을 채워주세요 !");
                    alert.show();

                    return;
                }

                if (checkidbtn.isEnabled()){
                    alert.setMessage("ID 중복확인을 해주세요 !");
                    alert.show();

                    return;
                }

                if (!userpw.getText().toString().equals(userpw2.getText().toString())){
                    alert.setMessage("PW를 다시 한 번 확인해주세요 ! ");
                    alert.show();

                    return;
                }

                if(userpw.getText().toString().length() < 8){
                    alert.setMessage("PW는 8글자 이상이여야 해요 !");
                    alert.show();

                    return;
                }


                userdao.Insert(new User(userid.getText().toString(), userpw.getText().toString(),
                        username.getText().toString(), usermail.getText().toString().concat("@").concat(maillist.getSelectedItem().toString())));

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

        cancelbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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
}
