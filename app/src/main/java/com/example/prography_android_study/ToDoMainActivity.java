package com.example.prography_android_study;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoMainActivity extends AppCompatActivity {

    private ArrayList<RecyclerViewItem> itemlist;
    private List<Memo> memolist;
    private RecyclerViewAdapter radapter;
    private ActionBar actionbar;

    String userid;

    MemoDB db;
    MemoDAO memodao;

    protected void LoadUserMemo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                memolist = memodao.GetUserMemos(userid);
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (Exception e) {
            e.getStackTrace();
        }

        for (int i = 0; i < memolist.size(); i++) {
            itemlist.add(new RecyclerViewItem(
                    memolist.get(i).title, memolist.get(i).content,
                    memolist.get(i).date, memolist.get(i).time));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);

        RecyclerView rview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager lmanager = new LinearLayoutManager(this);
        rview.setLayoutManager(lmanager);

        actionbar = getActionBar();
        userid = getIntent().getStringExtra("userid");

        itemlist = new ArrayList<>();

        db = MemoDB.getDatabase(getApplicationContext());
        memodao = db.memoDao();

        LoadUserMemo();

        radapter = new RecyclerViewAdapter(itemlist);
        rview.setAdapter(radapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rview.getContext(), lmanager.getOrientation());
        rview.addItemDecoration(dividerItemDecoration);


        radapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(ToDoMainActivity.this, MemoActivity.class);
                intent.putExtra("title", itemlist.get(position).getTitle());
                intent.putExtra("content", itemlist.get(position).getContent());
                intent.putExtra("date", itemlist.get(position).getDate());
                intent.putExtra("time", itemlist.get(position).getTime());
                intent.putExtra("pos", Integer.toString(position));
                startActivityForResult(intent, 3001);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                itemlist.remove(viewHolder.getLayoutPosition());
                radapter.notifyItemRemoved(viewHolder.getLayoutPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.writebtn) {
            Intent write = new Intent(ToDoMainActivity.this, MemoActivity.class);
            write.putExtra("title", "");
            write.putExtra("userid", userid);

            startActivityForResult(write, 3000);
        }

        if (id == R.id.searchbtn) {
            Toast.makeText(this, "검색 버튼", Toast.LENGTH_SHORT).show();

            return true;
        }

        if (id == R.id.logoutbtn) {
            SharedPreferences spclear = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = spclear.edit();

            edit.clear();
            edit.commit();

            Toast.makeText(ToDoMainActivity.this, "로그아웃 합니다.", Toast.LENGTH_LONG).show();

            Intent loginintent = new Intent(ToDoMainActivity.this, MainActivity.class);
            startActivity(loginintent);

            finish();
        }

        if (id == R.id.deleteallbtn) {
            AlertDialog.Builder alertbuilders = new AlertDialog.Builder(ToDoMainActivity.this);

            alertbuilders.setMessage("메모를 모두 삭제할까요 ?").setCancelable(false);

            alertbuilders.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    memodao.DeleteAllMemo(userid);
                                }
                            });

                            thread.start();

                            try {
                                thread.join();
                            } catch (Exception e) {
                                e.getStackTrace();
                            }

                            itemlist.clear();
                            radapter.notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(), "메모가 모두 삭제되었어요.", Toast.LENGTH_LONG).show();
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3000:
                    itemlist.add(new RecyclerViewItem(data.getStringExtra("title"), data.getStringExtra("content"),
                            data.getStringExtra("date").replaceAll(" ", ""), data.getStringExtra("time")));
                    radapter.notifyDataSetChanged();
                    break;
                case 3001:
                    switch (data.getStringExtra("del")) {
                        case "0":
                            itemlist.set(Integer.parseInt(data.getStringExtra("pos")),
                                    new RecyclerViewItem(data.getStringExtra("title"), data.getStringExtra("content"),
                                            data.getStringExtra("date").replaceAll(" ", ""), data.getStringExtra("time")));
                            break;
                        case "1":
                            itemlist.remove(Integer.parseInt(data.getStringExtra("pos")));
                            break;
                    }
                    radapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
