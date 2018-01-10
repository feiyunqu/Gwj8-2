package com.tdtf.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tdtf.gwj8.MyService;
import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.R;
import com.tdtf.gwj8.SerialOrder;
import com.tdtf.gwj8.Transform;
import com.tdtf.gwj8.activity.Mainmenu;
import com.tdtf.gwj8.myDataBase.Diary;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.myDataBase.MyDiary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MyDatabaseHelper dbHelper;
    MyDiary myDiary = new MyDiary();
    AutoCompleteTextView autouser;
    TextView textpower;
    EditText editPassword;
    Button btnlogin;
    SharedPreferences coordinate;
    ServiceConnection serviceConnection;
    MyService.MyBinder myBinder;
    MyService myService;
    FileOutputStream mOutputStream;
    Intent bindIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
                myService.setValues(new MyService.CallBacks() {
                    @Override
                    public void startRead(StringBuffer strBuffer) {

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
        startService(new Intent(this, MyService.class));
        bindIntent = new Intent(this, MyService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);

        Myutils.setArrayList_1(getSharedPreference("自定义一"));
        Myutils.setArrayList_2(getSharedPreference("自定义二"));

        dbHelper = new MyDatabaseHelper(this);
        final String[] struser = {"superdg", "111111"};
        autouser = (AutoCompleteTextView) findViewById(R.id.autoedit_user);
        textpower = (TextView) findViewById(R.id.text_power);
        try {
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                    "select _id,userName from User", null);
            inflateList(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editPassword = (EditText) findViewById(R.id.edit_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (autouser.getText().toString().equals(struser[0])
//                        & editPassword.getText().toString().equals(struser[1])) {
//                    Myutils.setPowername(textpower.getText().toString());
//                    Myutils.setUsername(autouser.getText().toString());
//                    Myutils.setPowerstring("11111111");
//                    startActivity(new Intent(MainActivity.this, Mainmenu.class));
//                } else {
                logining(autouser.getText().toString(), editPassword.getText().toString());
//                }
            }
        });

        ///////////////////////////////////////////////////
        Bundle bundle = getIntent().getExtras();
        String aaa = bundle.getString("username");
        String bbb = bundle.getString("password");
        String ccc = bundle.getString("permission");
        if (ccc == null) {
            ccc = "1";
        }
        autouser.setText(aaa);
        editPassword.setText(bbb);
        switch (ccc) {
            case "50":
                textpower.setText("超级用户");
                break;
            case "0":
                textpower.setText("系统管理员");
                break;
            case "1":
                textpower.setText("仪器维护员");
                break;
            case "2":
                textpower.setText("操作员");
                break;
            case "3":
                textpower.setText("审核员");
                break;
        }
        new Handler().postDelayed(new Runnable() {
            public void
            run() {
                //你需要跳转的地方的代码
                btnlogin.performClick();
            }
        }, 1500);//延迟2秒跳转  
    }

    public void logining(String user, String password) {
        Log.d("shunxu", "run: 25555555");
//        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
//                "select _id,password from User where userName=?", new String[]{user});
//        if (cursor.moveToFirst()) {//判断用户是否存在
//            String truepassword = cursor.getString(cursor.getColumnIndex("password"));
//            if (truepassword.equals(password)) {
        Myutils.setPowername(textpower.getText().toString());
        Myutils.setUsername(autouser.getText().toString());
        String strPowerString = Myutils.getPowerstring(dbHelper, textpower.getText().toString());
        Log.d("ssss", "logining: " + strPowerString);

        ////////发送用户名、时间、权限内容
        String userPowerHex = "";
        if (TextUtils.isEmpty(strPowerString)) {
            strPowerString = "00000000";
        }
        for (int i = 0; i < strPowerString.length(); i++) {
            userPowerHex += Transform.dec2hexTwo(strPowerString.substring(i, i + 1));
        }
        String sendUserPower = "aa0001" + userPowerHex + "cc33c33c";
        Log.d("ssss", "logining: " + sendUserPower);
        try {
            for (int i = 0; i < sendUserPower.length(); i = i + 2) {
                mOutputStream.write(Integer.parseInt(sendUserPower.substring(i, i + 2), 16));
            }
            Log.d("wwww", "logining: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //////////////
        byte[] userNameByte = new byte[0];
        try {
            userNameByte = Myutils.getUsername().getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String regex = "[\u4E00-\u9FA5]+";
        String userNameHex = "";
        if (Myutils.getUsername().matches(regex)) {
            for (int p = 0; p < userNameByte.length; p++) {
                userNameHex += Integer.toHexString((int) userNameByte[p] + 256);
            }
        } else {
            for (int p = 0; p < userNameByte.length; p++) {
                userNameHex += Integer.toHexString((int) userNameByte[p]);
            }
        }

        String userNameHexReal = userNameHex;
        for (int q = 0; q < (28 - userNameHex.length()) / 2; q++) {
            userNameHexReal += "20";
        }
        String sendUserName = "aa0002" + userNameHexReal + "cc33c33c";
        Log.d("ssss", "logining: " + sendUserName);
        try {
            Thread.sleep(100);
            for (int i = 0; i < sendUserName.length(); i = i + 2) {
                mOutputStream.write(Integer.parseInt(sendUserName.substring(i, i + 2), 16));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ////////////////////////////////
        String strTime = Myutils.specialFormatDateTime(System.currentTimeMillis());
        String strTimeHex = Transform.dec2hexTwo(strTime.substring(2, 4)) +
                Transform.dec2hexTwo(strTime.substring(4, 6)) +
                Transform.dec2hexTwo(strTime.substring(6, 8)) +
                Transform.dec2hexTwo(strTime.substring(8, 10)) +
                Transform.dec2hexTwo(strTime.substring(10, 12));
        String sendTime = "aa0003" + strTimeHex + "cc33c33c";
        try {
            Thread.sleep(100);
            for (int i = 0; i < sendTime.length(); i = i + 2) {
                mOutputStream.write(Integer.parseInt(sendTime.substring(i, i + 2), 16));
            }
            Log.d("wwww", "logining: ");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("ssss", "logining: " + sendTime);
        ////////////////////////////////

        myDiary.diary(dbHelper, Diary.login_userPower(autouser.getText().toString(), textpower.getText().toString()));
        autouser.setText("");
        editPassword.setText("");
        textpower.setText("");
        new Handler().postDelayed(new Runnable() {
            public void
            run() {
                //你需要跳转的地方的代码
                if (Myutils.isStartFlag()) {
                    Log.d("shunxu", "run: 3");
                    Myutils.setStartFlag(false);
                    startActivity(new Intent(MainActivity.this, Mainmenu.class));
                } else {
                    Log.d("shunxu", "run: 4");
                    Myutils.setStartFlag(false);
                    Myutils.setHandFlag(false);
                    Toast.makeText(getApplicationContext(),"启动异常，请先开启下位机或检查串口线是否已连接。",Toast.LENGTH_LONG).show();
                    stopService(new Intent(MainActivity.this, MyService.class));
                    finish();
                }
            }
        }, 1500);//延迟2秒跳转  
        unbindService(serviceConnection);
//            } else {
//                Toast.makeText(getApplicationContext(), "密码输入错误", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
//        }
//        cursor.close();
    }

    private void inflateList(Cursor cursor) {
        ArrayList<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex("userName")));
        }
        ArrayAdapter<String> autotext = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        autouser.setAdapter(autotext);
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(bindIntent);
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ffff", "onRestart: ");
        //unbindService(serviceConnection);
        Myutils.setStartFlag(false);
        Myutils.setHandFlag(false);
        stopService(new Intent(this, MyService.class));
        finish();
    }

    public ArrayList<String> getSharedPreference(String key) {
        String regularEx = "#";
        String[] str;
        coordinate = getSharedPreferences("arrayArgument", MODE_PRIVATE);
        String values;
        values = coordinate.getString(key, "");
        Myutils.setStandard(coordinate.getInt("standard", 8));
        Myutils.setDanWei(coordinate.getString("danwei", "0"));
        Myutils.setCiShu(coordinate.getString("cishu", "0"));
        Myutils.setShengjiang(coordinate.getString("shengjiang", "自动"));
        Myutils.setJiance(coordinate.getString("jiance", "自动"));
        Myutils.setYuzouliang(coordinate.getString("yuzouliang", "0.0"));
        Myutils.setQuyangliang(coordinate.getString("quyangliang", "0.0"));
        str = values.split(regularEx);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            arrayList.add(str[i]);
        }
        return arrayList;
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
