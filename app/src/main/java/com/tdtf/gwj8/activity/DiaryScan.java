package com.tdtf.gwj8.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.R;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.myDataBase.MyDiary;
import com.tdtf.gwj8.radioFragment.Searchpop;
import com.tdtf.gwj8.tools.MyReceive;

public class DiaryScan extends AppCompatActivity {
    MyDiary myDiary = new MyDiary();
    MyDatabaseHelper dbHelper;
    Button btnDiaryQuary;
    Button btnback;
    Searchpop searchpop;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    LinearLayout linearLayout;
    MyReceive myReceive = new MyReceive();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_scan);
        final TextClock textClock = (TextClock) findViewById(R.id.textClock7);
        textClock.setFormat24Hour("yyyy-MM-dd\nHH:mm:ss");
        final TextView textPower = (TextView) findViewById(R.id.text_power_9);
        textPower.setText("权限:\n" + Myutils.getPowername());
        final TextView textUser = (TextView) findViewById(R.id.text_user_9);
        textUser.setText("用户名:\n" + Myutils.getUsername());

        dbHelper = new MyDatabaseHelper(this);
        linearLayout = (LinearLayout) findViewById(R.id.fragLayout);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from Diary where dateTime like ?",
                new String[]{Myutils.formatDate(System.currentTimeMillis()) + "%"});//cursor里必须包含主键"_id"
        inflateList(cursor);

        ///////////////////////////////检测U盘插入
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        myIntentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        myIntentFilter.addDataScheme("file");
        //注册广播

        registerReceiver(myReceive, myIntentFilter);
        ///////////////////////

        btnDiaryQuary = (Button) findViewById(R.id.btn_diary_quary);
        btnDiaryQuary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper, "点击/检索/");
                if (searchpop == null) {
                    fragmentManager = getFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    searchpop = new Searchpop();
                    transaction.replace(R.id.fragLayout, searchpop);
                    transaction.commit();
                }
                if (linearLayout.getVisibility() == View.INVISIBLE) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnback = (Button) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myDiary.diary(dbHelper, "点击/返回/");
                //startActivity(new Intent(DiaryScan.this, Mainmenu.class));
                finish();
            }
        });
    }

    private void inflateList(Cursor cursor) {
        final ListView diarylist = (ListView) findViewById(R.id.diarylist);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.partner,
                cursor,
                new String[]{"_id", "dateTime", "context", "powerName", "userName"},
                new int[]{
                        R.id.item_layout,
                        R.id.item_dateTime,
                        R.id.item_context,
                        R.id.item_userName,
                        R.id.item_powerName},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        diarylist.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceive);
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
