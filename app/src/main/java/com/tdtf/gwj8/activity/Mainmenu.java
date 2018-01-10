package com.tdtf.gwj8.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.SQLException;
import android.os.IBinder;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.tdtf.gwj8.MyService;
import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.R;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.myDataBase.MyDiary;
import com.tdtf.gwj8.radioFragment.RadioButton0;
import com.tdtf.gwj8.radioFragment.RadioButton1;
import com.tdtf.gwj8.radioFragment.RadioButton2;
import com.tdtf.gwj8.tools.DiskTools;
import com.tdtf.gwj8.tools.MyReceive;

import java.io.FileOutputStream;
import java.io.IOException;

public class Mainmenu extends AppCompatActivity {
    MyDiary myDiary = new MyDiary();
    Button btnapply;
    Button btnDiary;
    Button btnData;
    Button btnExit;
    MyDatabaseHelper dbHelper;
    RadioGroup powergroup;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private RadioButton0 radioButton0;
    private RadioButton1 radioButton1;
    private RadioButton2 radioButton2;
    String powerdata0, powerdata1, powerdata2;
    ServiceConnection serviceConnection;
    MyService.MyBinder myBinder;
    MyService myService;
    FileOutputStream mOutputStream;
    MyReceive myReceive = new MyReceive();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        final TextClock textClock = (TextClock) findViewById(R.id.textClock);
        textClock.setFormat24Hour("yyyy-MM-dd\nHH:mm:ss");
        final TextView textPower = (TextView) findViewById(R.id.text_power);
        textPower.setText(Myutils.getPowername());
        final TextView textUser = (TextView) findViewById(R.id.text_user);
        textUser.setText(Myutils.getUsername());
        dbHelper = new MyDatabaseHelper(this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
                myService.setValues(new MyService.CallBacks() {
                    @Override
                    public void startRead(StringBuffer strBuffer) {
                        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from (select * from Diary where dateTime like ? order by dateTime desc limit 20) order by _id",
                                new String[]{Myutils.formatDate(System.currentTimeMillis()) + "%"});//cursor里必须包含主键"_id"
                        inflateList(cursor);
                    }

                    @Override
                    public void output(FileOutputStream outputStream) {
                        mOutputStream = outputStream;
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent bindIntent = new Intent(this, MyService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "select * from Power", null);
        if (!cursor.moveToFirst()) {
            dbHelper.getReadableDatabase().execSQL("insert into Power values(null,?,?)",
                    new String[]{"系统管理员", "11111111"});
            dbHelper.getReadableDatabase().execSQL("insert into Power values(null,?,?)",
                    new String[]{"操作员", "00000000"});
            dbHelper.getReadableDatabase().execSQL("insert into Power values(null,?,?)",
                    new String[]{"审核员", "00000000"});
        }
        cursor.close();
        ////////////////////////////////////////////////////////
        readSpace();
        powergroup = (RadioGroup) findViewById(R.id.power_group);
        powergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        myDiary.diary(dbHelper, "点击/系统管理员/");
                        fragmentManager = getFragmentManager();
                        transaction = fragmentManager.beginTransaction();
                        if (radioButton0 == null) {
                            radioButton0 = new RadioButton0();
                            transaction.add(R.id.radiofragment, radioButton0);
                        }
                        if (radioButton0 != null) {
                            transaction.hide(radioButton0);
                        }
                        if (radioButton1 != null) {
                            transaction.hide(radioButton1);
                        }
                        if (radioButton2 != null) {
                            transaction.hide(radioButton2);
                        }
                        transaction.show(radioButton0);
                        transaction.commit();
                        break;
                    case R.id.radioButton2:
                        myDiary.diary(dbHelper, "点击/操作员/");
                        fragmentManager = getFragmentManager();
                        transaction = fragmentManager.beginTransaction();
                        if (radioButton1 == null) {
                            radioButton1 = new RadioButton1();
                            transaction.add(R.id.radiofragment, radioButton1);
                        }
                        if (radioButton0 != null) {
                            transaction.hide(radioButton0);
                        }
                        if (radioButton1 != null) {
                            transaction.hide(radioButton1);
                        }
                        if (radioButton2 != null) {
                            transaction.hide(radioButton2);
                        }
                        transaction.show(radioButton1);
                        transaction.commit();
                        break;
                    case R.id.radioButton3:
                        myDiary.diary(dbHelper, "点击/审核员/");
                        fragmentManager = getFragmentManager();
                        transaction = fragmentManager.beginTransaction();
                        if (radioButton2 == null) {
                            radioButton2 = new RadioButton2();
                            transaction.add(R.id.radiofragment, radioButton2);
                        }
                        if (radioButton0 != null) {
                            transaction.hide(radioButton0);
                        }
                        if (radioButton1 != null) {
                            transaction.hide(radioButton1);
                        }
                        if (radioButton2 != null) {
                            transaction.hide(radioButton2);
                        }
                        transaction.show(radioButton2);
                        transaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });

        btnapply = (Button) findViewById(R.id.btn_apply);
        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper, "点击/保存设置/");
                try {
                    radioButton0.setpower(new RadioButton0.CallBacks() {
                        @Override
                        public void process(String str) {
                            powerdata0 = str;
                            powerOption("系统管理员", powerdata0);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    radioButton1.setpower(new RadioButton1.CallBacks() {
                        @Override
                        public void process(String str) {
                            powerdata1 = str;
                            powerOption("操作员", powerdata1);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    radioButton2.setpower(new RadioButton2.CallBacks() {
                        @Override
                        public void process(String str) {
                            powerdata2 = str;
                            powerOption("审核员", powerdata2);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (radioButton0 != null) {
                        dbHelper.getReadableDatabase().execSQL(
                                "update Power set powerData = ? where powerName = ?",
                                new String[]{powerdata0, "系统管理员"});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (radioButton1 != null) {
                        dbHelper.getReadableDatabase().execSQL(
                                "update Power set powerData = ? where powerName = ?",
                                new String[]{powerdata1, "操作员"});
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (radioButton2 != null) {
                        dbHelper.getReadableDatabase().execSQL(
                                "update Power set powerData = ? where powerName = ?",
                                new String[]{powerdata2, "审核员"});
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Mainmenu.this, "设置成功", Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////////////////////检测U盘插入
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        myIntentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        myIntentFilter.addDataScheme("file");
        //注册广播

        registerReceiver(myReceive, myIntentFilter);
        ///////////////////////

        btnDiary = (Button) findViewById(R.id.btnDiary);
        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper, "点击/日志查看/");
                startActivity(new Intent(Mainmenu.this, DiaryScan.class));
            }
        });

        btnData = (Button) findViewById(R.id.btnData);
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper, "点击/数据查看/");
                startActivity(new Intent(Mainmenu.this, Search.class));
            }
        });
        powerState(Myutils.getPowername());

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper, "点击/退出/");
                try {
                    String strExit = "aa0010cc33c33c";
                    for (int i = 0; i < strExit.length(); i = i + 2) {
                        mOutputStream.write(Integer.parseInt(strExit.substring(i, i + 2), 16));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                unbindService(serviceConnection);
                Myutils.setPowerstring(null);
                finish();
            }
        });
    }

    private void inflateList(Cursor cursor) {
        final ListView diaryList = (ListView) findViewById(R.id.diaryList);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_partner,
                cursor,
                new String[]{"context", "dateTime", "powerName", "userName"},
                new int[]{
                        R.id.item_context,
                        R.id.item_dateTime,
                        R.id.item_userName,
                        R.id.item_powerName},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        diaryList.setAdapter(adapter);
    }

    private void powerState(String flag) {
        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton radioButton3 = (RadioButton) findViewById(R.id.radioButton3);

        switch (flag) {
            case "超级用户":
                radioButton.performClick();
                break;
            case "系统管理员":
                radioButton.performClick();
                break;
            case "操作员":
                radioButton2.performClick();
                break;
            case "审核员":
                radioButton3.performClick();
                break;
        }

        if (flag.equals("系统管理员") || flag.equals("超级用户")) {
            radioButton.setEnabled(true);
            radioButton2.setEnabled(true);
            radioButton3.setEnabled(true);
            btnapply.setEnabled(true);
        } else {
            radioButton.setEnabled(false);
            radioButton2.setEnabled(false);
            radioButton3.setEnabled(false);
            btnapply.setEnabled(false);
        }
    }

    private void readSpace() {
        int a = DiskTools.getSystemDiskSpace(1);
        Log.d("read", "readSpace: " + a);
        if (a <= 50) {
            //DialogUtils.showInfoDialog(this, "磁盘空间不足，请及时清理！");
            AlertDialog.Builder builder = new AlertDialog.Builder(Mainmenu.this)
                    .setTitle("警告")
                    .setIcon(R.drawable.warning)
                    .setMessage("磁盘空间不足\n请及时清理！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        }
    }

    public void powerOption(String strPower, String strContext) {
        String[] strText = new String[8];
        strText[0] = "检测操作";
        strText[1] = "清洗操作";
        strText[2] = "检测设置";
        strText[3] = "通道设置";
        strText[4] = "打印设置";
        strText[5] = "通道标定";
        strText[6] = "预留设置";
        strText[7] = "预留设置";
        for (int i = 0; i < strContext.length(); i++) {
            if (strContext.charAt(i) == '1') {
                myDiary.diary(dbHelper, strPower + "权限设置：" + strText[i]);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from (select * from Diary where dateTime like ? order by dateTime desc limit 20) order by _id",
                new String[]{Myutils.formatDate(System.currentTimeMillis()) + "%"});//cursor里必须包含主键"_id"

        inflateList(cursor);
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
