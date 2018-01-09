package com.tdtf.gwj8.myDataBase;

import android.database.Cursor;
import android.util.Log;

import com.tdtf.gwj8.Myutils;

import java.util.ArrayList;

/**
 * Created by a on 2017/10/16.
 */

public class DataSort {
    /**
     * @param dbHelper  数据库句柄
     * @param biaozhi   数据库标志分类
     * @param lenth     检测次数
     * @param depth     通道数
     * @param strings_1 检测数据
     * @param strings_2 平均数据
     *                  麻醉器具
     */
    public static void dataMaZui(MyDatabaseHelper dbHelper, ArrayList<String> biaozhi, int lenth,
                                 int depth, String[] strings_1, String[] strings_2) {
        ArrayList<String> mazui46 = new ArrayList<>();
        ArrayList<String> mazui05 = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        for (int k = 0; k < lenth; k++) {
            mazui46.add(strings_1[depth * k]);
            mazui05.add(strings_1[depth * k + 1]);
        }
        for (int j = 0; j < depth; j++) {
            average.add(strings_2[j]);
        }

        int id;
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("select _id from DataMazui", null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex("_id")) + 1;
        } else {
            id = 1;
        }
        int k = -1;
        for (int i = 0; i < lenth; i++) {
            k++;
            dbHelper.getReadableDatabase().execSQL(
                    "insert into DataMazui values(null,?,?,?)", new String[]{
                            mazui46.get(i),
                            mazui05.get(i),
                            biaozhi.get(k) + String.valueOf(id)});
        }
        // TODO: 2017/6/5 插入均值
        dbHelper.getReadableDatabase().execSQL(
                "insert into DataMazui values(null,?,?,?)", new String[]{
                        average.get(0),
                        average.get(1),
                        biaozhi.get(12) + String.valueOf(id)});
        dbHelper.getReadableDatabase().execSQL(
                "insert into Dataname values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        Myutils.getSampleName(),
                        Myutils.getSampleNumber(),
                        Myutils.getSampleType(),
                        "a" + String.valueOf(id),
                        "b" + String.valueOf(id),
                        "c" + String.valueOf(id),
                        "d" + String.valueOf(id),
                        "e" + String.valueOf(id),
                        "f" + String.valueOf(id),
                        "g" + String.valueOf(id),
                        "h" + String.valueOf(id),
                        "i" + String.valueOf(id),
                        "j" + String.valueOf(id),
                        "k" + String.valueOf(id),
                        "l" + String.valueOf(id),
                        "z" + String.valueOf(id),
                        "麻醉器具",
                        "1",
                        Myutils.getDanWei(),
                        Myutils.getShengjiang(),
                        Myutils.getJiance(),
                        Myutils.getYuzouliang(),
                        Myutils.getQuyangliang(),
                        Myutils.getQuyangtou(),
                        Myutils.formatDateTime(System.currentTimeMillis())
                });
        cursor.close();
    }

    public static void dataYaoDian(MyDatabaseHelper dbHelper, ArrayList<String> biaozhi, int lenth,
                                   int depth, String[] strings_1, String[] strings_2) {
        ArrayList<String> yaodian15 = new ArrayList<>();
        ArrayList<String> yaodian25 = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        for (int k = 0; k < lenth; k++) {
            yaodian15.add(strings_1[depth * k]);
            yaodian25.add(strings_1[depth * k + 1]);
        }
        for (int j = 0; j < depth; j++) {
            average.add(strings_2[j]);
        }
        int id;
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("select _id from DataYaodian", null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex("_id")) + 1;
        } else {
            id = 1;
        }
        int k = -1;
        for (int i = 0; i < lenth; i++) {
            k++;
            dbHelper.getReadableDatabase().execSQL(
                    "insert into DataYaodian values(null,?,?,?)", new String[]{
                            yaodian15.get(i),
                            yaodian25.get(i),
                            biaozhi.get(k) + String.valueOf(id)});
        }
        dbHelper.getReadableDatabase().execSQL(
                "insert into DataYaodian values(null,?,?,?)", new String[]{
                        average.get(0),
                        average.get(1),
                        biaozhi.get(12) + String.valueOf(id)});
        dbHelper.getReadableDatabase().execSQL(
                "insert into Dataname values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        Myutils.getSampleName(),
                        Myutils.getSampleNumber(),
                        Myutils.getSampleType(),
                        "a" + String.valueOf(id),
                        "b" + String.valueOf(id),
                        "c" + String.valueOf(id),
                        "d" + String.valueOf(id),
                        "e" + String.valueOf(id),
                        "f" + String.valueOf(id),
                        "g" + String.valueOf(id),
                        "h" + String.valueOf(id),
                        "i" + String.valueOf(id),
                        "j" + String.valueOf(id),
                        "k" + String.valueOf(id),
                        "l" + String.valueOf(id),
                        "z" + String.valueOf(id),
                        "中国药典",
                        "5",
                        Myutils.getDanWei(),
                        Myutils.getShengjiang(),
                        Myutils.getJiance(),
                        Myutils.getYuzouliang(),
                        Myutils.getQuyangliang(),
                        Myutils.getQuyangtou(),
                        Myutils.formatDateTime(System.currentTimeMillis())
                });
        cursor.close();
    }

    public static void dataShuYe(MyDatabaseHelper dbHelper, ArrayList<String> biaozhi, int lenth,
                                 int depth, String[] strings_1, String[] strings_2) {
        ArrayList<String> wuran25 = new ArrayList<>();
        ArrayList<String> wuran50 = new ArrayList<>();
        ArrayList<String> wuran100 = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        for (int k = 0; k < lenth; k++) {
            wuran25.add(strings_1[depth * k]);
            wuran50.add(strings_1[depth * k + 1]);
            wuran100.add(strings_1[depth * k + 2]);
        }
        for (int j = 0; j < depth; j++) {
            average.add(strings_2[j]);
        }
        int id;
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("select _id from DataMazui", null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex("_id")) + 1;
        } else {
            id = 1;
        }
        int k = -1;
        for (int i = 0; i < lenth; i++) {
            k++;
            dbHelper.getReadableDatabase().execSQL(
                    "insert into DataWuran values(null,?,?,?,?)", new String[]{
                            wuran25.get(i),
                            wuran50.get(i),
                            wuran100.get(i),
                            biaozhi.get(k) + String.valueOf(id)});
        }
        dbHelper.getReadableDatabase().execSQL(
                "insert into DataWuran values(null,?,?,?,?)", new String[]{
                        average.get(0),
                        average.get(1),
                        average.get(2),
                        biaozhi.get(12) + String.valueOf(id)});
        dbHelper.getReadableDatabase().execSQL(
                "insert into Dataname values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        Myutils.getSampleName(),
                        Myutils.getSampleNumber(),
                        Myutils.getSampleType(),
                        "a" + String.valueOf(id),
                        "b" + String.valueOf(id),
                        "c" + String.valueOf(id),
                        "d" + String.valueOf(id),
                        "e" + String.valueOf(id),
                        "f" + String.valueOf(id),
                        "g" + String.valueOf(id),
                        "h" + String.valueOf(id),
                        "i" + String.valueOf(id),
                        "j" + String.valueOf(id),
                        "k" + String.valueOf(id),
                        "l" + String.valueOf(id),
                        "z" + String.valueOf(id),
                        "输液器具-污染",
                        "3",
                        Myutils.getDanWei(),
                        Myutils.getShengjiang(),
                        Myutils.getJiance(),
                        Myutils.getYuzouliang(),
                        Myutils.getQuyangliang(),
                        Myutils.getQuyangtou(),
                        Myutils.formatDateTime(System.currentTimeMillis())
                });
        cursor.close();
    }

    public static void dataLvChu(MyDatabaseHelper dbHelper, ArrayList<String> biaozhi, int lenth,
                                 int depth, String[] strings_1, String[] strings_2) {
        ArrayList<String> lvchu20 = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        for (int k = 0; k < lenth; k++) {
            lvchu20.add(strings_1[depth * k]);
        }
        for (int j = 0; j < depth; j++) {
            average.add(strings_2[j]);
        }
        int id;
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("select _id from DataLvchu", null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex("_id")) + 1;
        } else {
            id = 1;
        }
        int k = -1;
        for (int i = 0; i < lenth; i++) {
            k++;
            dbHelper.getReadableDatabase().execSQL(
                    "insert into DataLvchu values(null,?,?)", new String[]{
                            lvchu20.get(i),
                            biaozhi.get(k) + String.valueOf(id)});
        }
        dbHelper.getReadableDatabase().execSQL(
                "insert into DataLvchu values(null,?,?)", new String[]{
                        average.get(0),
                        biaozhi.get(12) + String.valueOf(id)});

        dbHelper.getReadableDatabase().execSQL(
                "insert into Dataname values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        Myutils.getSampleName(),
                        Myutils.getSampleNumber(),
                        Myutils.getSampleType(),
                        "a" + String.valueOf(id),
                        "b" + String.valueOf(id),
                        "c" + String.valueOf(id),
                        "d" + String.valueOf(id),
                        "e" + String.valueOf(id),
                        "f" + String.valueOf(id),
                        "g" + String.valueOf(id),
                        "h" + String.valueOf(id),
                        "i" + String.valueOf(id),
                        "j" + String.valueOf(id),
                        "k" + String.valueOf(id),
                        "l" + String.valueOf(id),
                        "z" + String.valueOf(id),
                        "输液器具-滤除",
                        "2",
                        Myutils.getDanWei(),
                        Myutils.getShengjiang(),
                        Myutils.getJiance(),
                        Myutils.getYuzouliang(),
                        Myutils.getQuyangliang(),
                        Myutils.getQuyangtou(),
                        Myutils.formatDateTime(System.currentTimeMillis())
                });
        cursor.close();
    }

    /**
     * @param dbHelper  数据库句柄
     * @param biaozhi   数据库标志分类
     * @param lenth     检测次数
     * @param depth     检测通道
     * @param strings_1 积分数据
     * @param strings_2 微分数据
     * @param strings_3 积分平均
     * @param strings_4 微分平均
     */
    public static void dataZiDingYi(MyDatabaseHelper dbHelper, ArrayList<String> biaozhi, int lenth,
                                    int depth, String[] strings_1, String[] strings_2,
                                    String[] strings_3, String[] strings_4, String string) {
        ArrayList<String> lijing = new ArrayList<>();
        ArrayList<String> jifen = new ArrayList<>();
        ArrayList<String> weifen = new ArrayList<>();
        ArrayList<String> averageJiFen = new ArrayList<>();
        ArrayList<String> averageWeiFen = new ArrayList<>();
        switch (Myutils.getStandard()) {
            case 3:
                lijing = Myutils.getArrayList_1();
                break;
            case 4:
                lijing = Myutils.getArrayList_2();
                break;
        }

        for (int k = 0; k < strings_1.length; k++) {
            jifen.add(strings_1[k]);
            weifen.add(strings_2[k]);
            Log.d("tyu", "dataList:============ " + strings_1[k]);
            Log.d("tyu", "dataList:------------ " + strings_2[k]);
        }
        for (int j = 0; j < depth; j++) {
            averageJiFen.add(strings_3[j]);
            averageWeiFen.add(strings_4[j]);
        }
        int id;
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("select _id from Data", null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex("_id")) + 1;
        } else {
            id = 1;
        }
        int k = -1;
        for (int i = 0; i < lenth * depth; i++) {
            if (i % 8 == 0) {
                k++;
            }
            dbHelper.getReadableDatabase().execSQL(
                    "insert into Data values(null,?,?,?,?)", new String[]{
                            lijing.get(i-8*k),
                            weifen.get(i),
                            jifen.get(i),
                            biaozhi.get(k) + String.valueOf(id)});
        }
        for (int m = 0; m < 8; m++) {
            dbHelper.getReadableDatabase().execSQL(
                    "insert into Data values(null,?,?,?,?)", new String[]{
                            lijing.get(m),
                            averageWeiFen.get(m),
                            averageJiFen.get(m),
                            biaozhi.get(12) + String.valueOf(id)});
        }

        dbHelper.getReadableDatabase().execSQL(
                "insert into Dataname values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{
                        Myutils.getSampleName(),
                        Myutils.getSampleNumber(),
                        Myutils.getSampleType(),
                        "a" + String.valueOf(id),
                        "b" + String.valueOf(id),
                        "c" + String.valueOf(id),
                        "d" + String.valueOf(id),
                        "e" + String.valueOf(id),
                        "f" + String.valueOf(id),
                        "g" + String.valueOf(id),
                        "h" + String.valueOf(id),
                        "i" + String.valueOf(id),
                        "j" + String.valueOf(id),
                        "k" + String.valueOf(id),
                        "l" + String.valueOf(id),
                        "z" + String.valueOf(id),
                        string,
                        "0",
                        Myutils.getDanWei(),
                        Myutils.getShengjiang(),
                        Myutils.getJiance(),
                        Myutils.getYuzouliang(),
                        Myutils.getQuyangliang(),
                        Myutils.getQuyangtou(),
                        Myutils.formatDateTime(System.currentTimeMillis())
                });
        cursor.close();
    }
}
