package com.tdtf.gwj8.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.R;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.myDataBase.MyDiary;
import com.tdtf.gwj8.tools.MyReceive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends AppCompatActivity {
    MyDiary myDiary=new MyDiary();
    MyDatabaseHelper dbHelper;
    ListView partner;
    ListPopupWindow popupWindow;
    ArrayList<String> biaozhun;

    String startdatepick;
    String enddatepick;

    Button btndatepickstart;
    Button btndatepickend;
    Button btnbiaozhun;
    Button btnquery;
    Button btnback;
    EditText editname;
    MyReceive myReceive=new MyReceive();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final TextClock textClock = (TextClock) findViewById(R.id.textClock10);
        textClock.setFormat24Hour("yyyy-MM-dd\nHH:mm:ss");
        final TextView textPower = (TextView) findViewById(R.id.text_power_12);
        textPower.setText("权限:\n" + Myutils.getPowername());
        final TextView textUser = (TextView) findViewById(R.id.text_user_12);
        textUser.setText("用户名:\n" + Myutils.getUsername());
        dbHelper = new MyDatabaseHelper(this);
        editname = (EditText) findViewById(R.id.edit_name);

        partner = (ListView) findViewById(R.id.partner_list);
        partner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/2/21 点击item后进入数据显示界面
                Cursor wrapper = (Cursor) partner.getItemAtPosition(i);
                String string_name = wrapper.getString(wrapper.getColumnIndex("name"));
                String string_pihao = wrapper.getString(wrapper.getColumnIndex("pihao"));
                String string_date = wrapper.getString(wrapper.getColumnIndex("dateTime"));
                Intent intent = new Intent(Search.this, DataQuary.class) ;
                intent.putExtra("name", string_name);
                intent.putExtra("dateTime", string_date);
                intent.putExtra("pihao", string_pihao);
                startActivity(intent);
            }
        });

        String sql = "select * from Dataname order by dateTime desc limit 20";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);//cursor里必须包含主键"_id"
        inflateList(cursor);

        ///////////////////////////////检测U盘插入
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        myIntentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        myIntentFilter.addDataScheme("file");
        //注册广播

        registerReceiver(myReceive, myIntentFilter);
        ///////////////////////

        btndatepickstart = (Button) findViewById(R.id.btndatepickstart);
        btndatepickstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper,"点击/选择起始日期/");
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(Search.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        btndatepickstart.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                        String mm, dd;
                        if (month < 9) {
                            mm = "0" + (month + 1);
                        } else {
                            mm = String.valueOf(month + 1);
                        }
                        if (dayOfMonth < 9) {
                            dd = "0" + dayOfMonth;
                        } else {
                            dd = String.valueOf(dayOfMonth);
                        }
                        startdatepick = year + "-" + mm + "-" + dd;
                    }
                }
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btndatepickend = (Button) findViewById(R.id.btndatepickend);
        btndatepickend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper,"点击/选择结束日期/");
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(Search.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        btndatepickend.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                        String mm, dd;
                        if (month < 9) {
                            mm = "0" + (month + 1);
                        } else {
                            mm = String.valueOf(month + 1);
                        }
                        if (dayOfMonth < 9) {
                            dd = "0" + dayOfMonth;
                        } else {
                            dd = String.valueOf(dayOfMonth);
                        }
                        enddatepick = year + "-" + mm + "-" + dd;
                    }
                }
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnbiaozhun = (Button) findViewById(R.id.btnbiaozhun);
        biaozhun = new ArrayList<>();
        biaozhun.add("中国药典");
        biaozhun.add("输液器具-污染");
        biaozhun.add("输液器具-滤除");
        biaozhun.add("麻醉器具");
        biaozhun.add("自定义一");
        biaozhun.add("自定义二");

        popupWindow = new ListPopupWindow(this);
        popupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, biaozhun) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.parseColor("#ff000000"));
                tv.setTextSize(24);
                return tv;
            }
        });
        popupWindow.setAnchorView(btnbiaozhun);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setModal(true);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                btnbiaozhun.setText(biaozhun.get(position));
                myDiary.diary(dbHelper,"标准选择为"+biaozhun.get(position));
                popupWindow.dismiss();
            }
        });
        btnbiaozhun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper,"点击/选择标准/");
                popupWindow.show();
            }
        });

        btnquery = (Button) findViewById(R.id.btnquery);
        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDiary.diary(dbHelper,"点击/检索/");
                myDiary.diary(dbHelper,"输入关键字："+editname.getText().toString());
                String sql = "select * from Dataname where 1=1";
                String sqlstartdate = "00:00:00";
                String sqlenddate = "23:59:59";
                ArrayList<String> arrays = new ArrayList<>();
                String quaryCombo = "";

                if (!TextUtils.isEmpty(editname.getText())) {
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher isNum = pattern.matcher(editname.getText().toString());
                    if (!isNum.matches()) {
                        quaryCombo = " and name like ?";
                    } else {
                        quaryCombo = " and pihao like ?";
                    }
                    arrays.add("%" + editname.getText().toString() + "%");
                }
                if (!btnbiaozhun.getText().toString().equals("选择标准")) {
                    if (TextUtils.isEmpty(quaryCombo)) {
                        quaryCombo = " and fragment= ?";
                        arrays.add(btnbiaozhun.getText().toString());
                    } else {
                        quaryCombo = quaryCombo + " and fragment= ?";
                        arrays.add(btnbiaozhun.getText().toString());
                    }
                }
                if (!btndatepickstart.getText().toString().equals("选择起始日期")) {
                    arrays.add(startdatepick + " " + sqlstartdate);
                    if (TextUtils.isEmpty(quaryCombo)) {
                        quaryCombo = " and dateTime between ? and ?";
                    } else {
                        quaryCombo = quaryCombo + " and dateTime between ? and ?";
                    }

                    if (!btndatepickend.getText().toString().equals("选择结束日期")) {
                        arrays.add(enddatepick + " " + sqlenddate);
                    }else {
                        Toast.makeText(Search.this,"请选择结束日期",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (!btndatepickend.getText().toString().equals("选择结束日期")) {
                        Toast.makeText(Search.this,"请选择起始日期",Toast.LENGTH_SHORT).show();
                    }
                }

                sql = sql + quaryCombo;
                String[] strings = arrays.toArray(new String[arrays.size()]);
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, strings);//cursor里必须包含主键"_id"
                inflateList(cursor);
            }
        });

        btnback = (Button) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper,"点击/返回/");
                //startActivity(new Intent(Search.this, Mainmenu.class));
                finish();
            }
        });
    }

    private void inflateList(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.partner,
                cursor,
                new String[]{"name", "pihao", "dateTime", "fragment"},
                new int[]{R.id.item_layout, R.id.item_dateTime, R.id.item_context, R.id.item_userName},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        partner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

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
