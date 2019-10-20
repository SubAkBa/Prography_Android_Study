package com.example.prography_android_study;

import android.app.ActionBar;
import android.app.Activity;
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

public class ToDoMainActivity extends AppCompatActivity {

    private ArrayList<RecyclerViewItem> itemlist;
    private RecyclerViewAdapter radapter;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);

        RecyclerView rview = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager lmanager = new LinearLayoutManager(this);
        rview.setLayoutManager(lmanager);


        actionbar = getActionBar();

        itemlist = new ArrayList<>();

        radapter = new RecyclerViewAdapter(itemlist);
        rview.setAdapter(radapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rview.getContext(), lmanager.getOrientation());
        rview.addItemDecoration(dividerItemDecoration);

        SwipeController sc = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                itemlist.remove(position);
                radapter.notifyItemRemoved(position);
                radapter.notifyItemRangeChanged(position, radapter.getItemCount());
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(sc);
        itemTouchhelper.attachToRecyclerView(rview);

//        radapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                Intent intent = new Intent(ToDoMainActivity.this, MemoActivity.class);
//                intent.putExtra("title", itemlist.get(position).getTitle());
//                intent.putExtra("content", itemlist.get(position).getContent());
//                intent.putExtra("date", itemlist.get(position).getDate());
//                intent.putExtra("time", itemlist.get(position).getTime());
//                intent.putExtra("pos", position);
//                setResult(RESULT_OK, intent);
//                startActivityForResult(intent, 3001);
//            }
//        });

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
                    itemlist.add(new RecyclerViewItem(data.getStringExtra("title"), data.getStringExtra("content"),
                            data.getStringExtra("date").replaceAll(" ", ""), data.getStringExtra("time")));
                    radapter.notifyDataSetChanged();
                    break;
                case 3001:
                    itemlist.set(Integer.parseInt(data.getStringExtra("pos")),
                            new RecyclerViewItem(data.getStringExtra("title"), data.getStringExtra("content"),
                            data.getStringExtra("date").replaceAll(" ", ""), data.getStringExtra("time")));
                    radapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
