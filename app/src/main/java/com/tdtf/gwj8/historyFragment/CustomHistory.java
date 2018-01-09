package com.tdtf.gwj8.historyFragment;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.R;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.myDataBase.MyDiary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomHistory extends Fragment {
    MyDiary myDiary=new MyDiary();
    View rootView;
    MyDatabaseHelper dbHelper;
    int n = 0;
    int cishu;
    boolean flag = true;
    String string_name, string_pihao;
    ArrayList<String> jifen = new ArrayList<>();
    ArrayList<String> weifen = new ArrayList<>();
    ArrayList<String> lijing = new ArrayList<>();
    Button btnnext;
    Button btnaverage;
    Button btndelet;
    Button btnclear;
    Button btnsave;
    Button btnback;
    EditText edit_name, edit_pihao;
    TextView text_name, text_pihao;
    TextView text_shengjiang,text_jiance,text_yuzou,text_quyang;
    TextView[] ttv;
    TextView[] tv;
    TextView[] wtv;
    TextView textTimes;

    String print_time;
    TextView textGuiGe;
    TextView textunit;

    public CustomHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_custom_history, container, false);
        dbHelper = new MyDatabaseHelper(getActivity());
        string_name = getActivity().getIntent().getStringExtra("name");
        string_pihao = getActivity().getIntent().getStringExtra("pihao");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
        print_time = formatter.format(curDate);

        textunit = (TextView) rootView.findViewById(R.id.text_unit);
        textGuiGe = (TextView) rootView.findViewById(R.id.text_guige);
        text_shengjiang = (TextView) rootView.findViewById(R.id.text_shengjiang);
        text_jiance = (TextView) rootView.findViewById(R.id.text_jiance);
        text_yuzou = (TextView) rootView.findViewById(R.id.text_yuzou);
        text_quyang = (TextView) rootView.findViewById(R.id.text_quyang);

        btndelet = (Button) rootView.findViewById(R.id.btndelet);
        btnclear = (Button) rootView.findViewById(R.id.btnclear);
        btnsave = (Button) rootView.findViewById(R.id.btnsave);
        edit_name = (EditText) rootView.findViewById(R.id.edit_name);
        text_name = (TextView) rootView.findViewById(R.id.text_name);
        edit_pihao = (EditText) rootView.findViewById(R.id.edit_pihao);
        text_pihao = (TextView) rootView.findViewById(R.id.text_pihao);

        btndelet.setVisibility(View.GONE);
        btnclear.setVisibility(View.GONE);
        btnsave.setVisibility(View.GONE);
        edit_name.setVisibility(View.GONE);
        text_name.setText(string_name);
        edit_pihao.setVisibility(View.GONE);
        text_pihao.setText(string_pihao);

        textTimes = (TextView) rootView.findViewById(R.id.text_times);
        // TODO: 2016/12/15  粒径
        ttv = new TextView[8];
        ttv[0] = (TextView) rootView.findViewById(R.id.textView136);
        ttv[1] = (TextView) rootView.findViewById(R.id.textView130);
        ttv[2] = (TextView) rootView.findViewById(R.id.textView124);
        ttv[3] = (TextView) rootView.findViewById(R.id.textView118);
        ttv[4] = (TextView) rootView.findViewById(R.id.textView112);
        ttv[5] = (TextView) rootView.findViewById(R.id.textView106);
        ttv[6] = (TextView) rootView.findViewById(R.id.textView100);
        ttv[7] = (TextView) rootView.findViewById(R.id.textView94);
        //TODO: 2016/12/14 积分
        tv = new TextView[8];
        tv[0] = (TextView) rootView.findViewById(R.id.textView134);
        tv[1] = (TextView) rootView.findViewById(R.id.textView128);
        tv[2] = (TextView) rootView.findViewById(R.id.textView122);
        tv[3] = (TextView) rootView.findViewById(R.id.textView116);
        tv[4] = (TextView) rootView.findViewById(R.id.textView110);
        tv[5] = (TextView) rootView.findViewById(R.id.textView104);
        tv[6] = (TextView) rootView.findViewById(R.id.textView98);
        tv[7] = (TextView) rootView.findViewById(R.id.textView92);
        // TODO: 2016/12/14 微分
        wtv = new TextView[8];
        wtv[0] = (TextView) rootView.findViewById(R.id.textView135);
        wtv[1] = (TextView) rootView.findViewById(R.id.textView129);
        wtv[2] = (TextView) rootView.findViewById(R.id.textView123);
        wtv[3] = (TextView) rootView.findViewById(R.id.textView117);
        wtv[4] = (TextView) rootView.findViewById(R.id.textView111);
        wtv[5] = (TextView) rootView.findViewById(R.id.textView105);
        wtv[6] = (TextView) rootView.findViewById(R.id.textView99);
        wtv[7] = (TextView) rootView.findViewById(R.id.textView93);

        for (int k = 0; k < 13; k++) {
            dataList(lijing, "particle", Myutils.DATA_TIMES[k], string_name);
            dataList(weifen, "differential", Myutils.DATA_TIMES[k], string_name);
            dataList(jifen, "integral", Myutils.DATA_TIMES[k], string_name);
        }
        cishu = (lijing.size() - 8) / 8;

        for (int l = 0; l < 8; l++) {
            ttv[l].setText(lijing.get(l));
            tv[l].setText(jifen.get(l));
            wtv[l].setText(weifen.get(l));
        }

        String sqlstr = "select guige,danwei,shengjiang,jiance,yuzouliang,quyangliang,quyangtou from Dataname where name=?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlstr, new String[]{string_name});
        if (cursor.moveToFirst()){
            textGuiGe.setText(cursor.getString(cursor.getColumnIndex("guige")));
            textunit.setText(cursor.getString(cursor.getColumnIndex("danwei")));
            text_shengjiang.setText(cursor.getString(cursor.getColumnIndex("shengjiang")));
            text_jiance.setText(cursor.getString(cursor.getColumnIndex("jiance")));
            text_yuzou.setText(cursor.getString(cursor.getColumnIndex("yuzouliang"))+"ml");
            text_quyang.setText(cursor.getString(cursor.getColumnIndex("quyangliang"))+"ml");
        }
        cursor.close();

        btnnext = (Button) rootView.findViewById(R.id.btnnext);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n < cishu) {
                    for (int l = 8 * n; l < 8 + 8 * n; l++) {
                        wtv[l - 8 * n].setText(weifen.get(l));
                        tv[l - 8 * n].setText(jifen.get(l));
                        ttv[l - 8 * n].setText(lijing.get(l));
                    }
                    flag = true;
                    n++;
                    textTimes.setText(String.valueOf(n));
                } else if (n == cishu) {
                    for (int l = 0; l < 8; l++) {
                        wtv[l].setText(weifen.get(weifen.size() - 8 + l));
                        tv[l].setText(jifen.get(jifen.size() - 8 + l));
                        ttv[l].setText(lijing.get(l));
                    }
                    flag = false;
                    n++;
                    textTimes.setText("均值");
                } else if (n > cishu) {
                    for (int l = 0; l < 8; l++) {
                        wtv[l].setText(weifen.get(l));
                        tv[l].setText(jifen.get(l));
                        ttv[l].setText(lijing.get(l));
                    }
                    flag = true;
                    n = 1;
                    textTimes.setText(String.valueOf(n));
                }
            }
        });
        btnaverage = (Button) rootView.findViewById(R.id.btnaverage);
        btnaverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int l = 0; l < 8; l++) {
                    wtv[l].setText(weifen.get(weifen.size() - 8 + l));
                    tv[l].setText(jifen.get(jifen.size() - 8 + l));
                    ttv[l].setText(lijing.get(l));
                }
                flag = false;
                textTimes.setText("均值");
            }
        });

        btnback = (Button) getActivity().findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDiary.diary(dbHelper,"点击/返回/");
                getActivity().finish();
            }
        });
        return rootView;
    }
    private void dataList(ArrayList<String> array_data, String string, String times, String names) {
        String sqlstr = "select particle,differential,integral from Data " +
                "where sid=(select " + times + " from Dataname where name=?)";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlstr, new String[]{names});
        while (cursor.moveToNext()) {
            array_data.add(cursor.getString(cursor.getColumnIndex(string)));
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //退出程序时关闭MyDatabaseHelper里的SQLiteDatabase
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
