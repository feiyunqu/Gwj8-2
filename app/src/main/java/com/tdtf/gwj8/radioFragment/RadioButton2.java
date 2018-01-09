package com.tdtf.gwj8.radioFragment;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioButton2 extends Fragment {
    MyDatabaseHelper dbhelper;
    String powerdata;
    StringBuilder strBuilder;
    CheckBox[] checkBoxes;

    public RadioButton2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_radio_button2, container, false);
        checkBoxes = new CheckBox[8];
        checkBoxes[0] = (CheckBox) view.findViewById(R.id.cb_jianCeCaoZuo);
        checkBoxes[1] = (CheckBox) view.findViewById(R.id.cb_qingXiCaoZuo);
        checkBoxes[2] = (CheckBox) view.findViewById(R.id.cb_JianCeSheZhi);
        checkBoxes[3] = (CheckBox) view.findViewById(R.id.cb_TongDaoSheZhi);
        checkBoxes[4] = (CheckBox) view.findViewById(R.id.cb_daYinSheZhi);
        checkBoxes[5] = (CheckBox) view.findViewById(R.id.cb_TongDaoBiaoDing);
        checkBoxes[6] = (CheckBox) view.findViewById(R.id.cb_yuLiu0);
        checkBoxes[7] = (CheckBox) view.findViewById(R.id.cb_yuLiu1);

        dbhelper = new MyDatabaseHelper(getActivity());
        Cursor cursor = dbhelper.getReadableDatabase().rawQuery(
                "select powerData from Power where powerName=?", new String[]{"审核员"});
        if (cursor.moveToFirst()) {
            powerdata = cursor.getString(cursor.getColumnIndex("powerData"));
            Log.d("ggggg", "onCreateView: " + powerdata);
        } else {
            powerdata = "00000000";
        }
        cursor.close();

        byte[] bytePower = powerdata.getBytes();
        for (int i=0;i<bytePower.length;i++){
            if (bytePower[i]=='0'){
                checkBoxes[i].setChecked(false);
            }else {
                checkBoxes[i].setChecked(true);
            }
        }
        if (Myutils.getPowername().equals("系统管理员")||Myutils.getPowername().equals("超级用户")) {
            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].setEnabled(true);
            }
        } else {
            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].setEnabled(false);
            }
        }
        strBuilder = new StringBuilder(powerdata);
        checkBoxes[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(0, '1');
                } else {
                    strBuilder.setCharAt(0, '0');
                }
            }
        });
        checkBoxes[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(1, '1');
                } else {
                    strBuilder.setCharAt(1, '0');
                }
            }
        });
        checkBoxes[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(2, '1');
                } else {
                    strBuilder.setCharAt(2, '0');
                }
            }
        });
        checkBoxes[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(3, '1');
                } else {
                    strBuilder.setCharAt(3, '0');
                }
            }
        });
        checkBoxes[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(4, '1');
                } else {
                    strBuilder.setCharAt(4, '0');
                }
            }
        });
        checkBoxes[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(5, '1');
                } else {
                    strBuilder.setCharAt(5, '0');
                }
            }
        });
        checkBoxes[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(6, '1');
                } else {
                    strBuilder.setCharAt(6, '0');
                }
            }
        });
        checkBoxes[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strBuilder.setCharAt(7, '1');
                } else {
                    strBuilder.setCharAt(7, '0');
                }
            }
        });
        return view;
    }

    public interface CallBacks {
        void process(String str);
    }

    public void setpower(CallBacks callBacks) {
        callBacks.process(strBuilder.toString());
    }
}
