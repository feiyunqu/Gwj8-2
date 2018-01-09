package com.tdtf.gwj8.historyFragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MazuiHistory extends Fragment {
    MyDiary myDiary=new MyDiary();
    View rootView;
    MyDatabaseHelper dbHelper;

    int n = 1;
    int cishu;
    boolean flag = true;
    String string_name, string_pihao;

    ArrayList<String> mazui46 = new ArrayList<>();
    ArrayList<String> mazui05 = new ArrayList<>();

    Button btnnext;
    Button btnaverage;
    Button btndelet;
    Button btnclear;
    Button btnsave;
    Button btnback;
    EditText edit_name, edit_pihao;
    TextView text_name, text_pihao;
    TextView text46;
    TextView text05;
    TextView textTimes;
    TextView text_shengjiang,text_jiance,text_yuzou,text_quyang;
    String print_time;
    TextView textGuiGe;
    TextView textunit;

    public MazuiHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_mazui_history, container, false);
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

        text46 = (TextView) rootView.findViewById(R.id.text_46);
        text05 = (TextView) rootView.findViewById(R.id.text_5);
        textTimes = (TextView) rootView.findViewById(R.id.text_times);

        for (int k = 0; k < 13; k++) {
            dataList(mazui46, "number46", Myutils.DATA_TIMES[k], string_name);
            dataList(mazui05, "number05", Myutils.DATA_TIMES[k], string_name);
        }

        cishu = mazui46.size() - 1;

        text46.setText(mazui46.get(0));
        text05.setText(mazui05.get(0));

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
                    text46.setText(mazui46.get(n));
                    text05.setText(mazui05.get(n));
                    flag = true;
                    n++;
                    textTimes.setText(String.valueOf(n));
                } else if (n == cishu) {
                    text46.setText(mazui46.get(cishu));
                    text05.setText(mazui05.get(cishu));
                    flag = false;
                    n++;
                    textTimes.setText("均值");
                } else if (n > cishu) {
                    text46.setText(mazui46.get(0));
                    text05.setText(mazui05.get(0));
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
                text46.setText(mazui46.get(cishu));
                text05.setText(mazui05.get(cishu));
                textTimes.setText("均值");
                flag = false;
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
        String sqlstr = "select number46,number05 from DataMazui " +
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
