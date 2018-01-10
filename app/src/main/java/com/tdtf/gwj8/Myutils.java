package com.tdtf.gwj8;

import android.database.Cursor;
import android.util.Log;

import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by a on 2017/2/20.
 */

public class Myutils {
    //    public static final String[] POWER_CLASS = {"管理员", "操作员", "维护员"};
    public static final String[] DATA_TIMES = {"first", "second", "third", "fourth", "fifth",
            "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth", "average"};
    public static final String[] BUTTON_NAME = {
            "开始检测", "打印设置", "检测方式", "取样方式", "预走量",
            "取样量", "检测次数", "取样位置", "计数单位", "麻醉器具",
            "8386滤除", "8386-05污染", "8386-98污染", "中国药典", "自定义",
            "自定义设置", "开机清洗设置", "关机清洗设置", "反冲", "标定操作",
            "标尺设置", "标定参数", "噪声测定", "用户设置", "修正参数"};

    public static String formatDate(long time) {
        String part = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(part, Locale.getDefault());
        return formatter.format(new Date(time));
    }

    public static String formatDateTime(long time) {
        String part = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(part, Locale.getDefault());
        return formatter.format(new Date(time));
    }

    public static String specialFormatDateTime(long time) {
        String part = "yyyyMMddHHmm";
        SimpleDateFormat formatter = new SimpleDateFormat(part, Locale.getDefault());
        return formatter.format(new Date(time));
    }

//    public static String formatShortTime(long time) {
//        int count = (int) (time / 1000);
//        int hour = count / 3600;
//        int minute = count % 3600 / 60;
//        int second = count % 60;
//        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
//    }

    private static String powerstring;//权限分配
    private static String powername = "";//权限名称
    private static String username = "";//用户名
    private static String sampleName = "";//样品名称
    private static String sampleNumber = "";//样品批号
    private static String sampleType = "";//样品规格
    private static ArrayList<String> arrayList_1;//自定义一的通道粒径
    private static ArrayList<String> arrayList_2;//自定义二的通道粒径
    private static String danWei = "0";//单位：XX/ml
    private static String ciShu = "0";//检测次数
    private static String shengjiang = "自动";
    private static String jiance = "自动";
    private static String yuzouliang = "0";
    private static String quyangliang = "0";
    private static String quyangtou = "90";

    public static void setPowerstring(String powerstring) {
        Myutils.powerstring = powerstring;
    }

    public static String getPowerstring(MyDatabaseHelper myDatabaseHelper, String name) {
        Log.d("ssss", "logining: " + name);
        if (powerstring == null) {
            if (name.equals("超级用户")) {
                powerstring = "11111111";
            } else {
                try {
                    Cursor cursor = myDatabaseHelper.getReadableDatabase().rawQuery(
                            "select _id,powerData from Power where powerName=?", new String[]{name});
                    while (cursor.moveToNext()) {
                        powerstring = cursor.getString(cursor.getColumnIndex("powerData"));
                    }
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return powerstring;
    }

    public static String getJiance() {
        return jiance;
    }

    public static void setJiance(String jiance) {
        Myutils.jiance = jiance;
    }

    public static String getYuzouliang() {
        return yuzouliang;
    }

    public static void setYuzouliang(String yuzouliang) {
        Myutils.yuzouliang = yuzouliang;
    }

    public static String getQuyangliang() {
        return quyangliang;
    }

    public static void setQuyangliang(String quyangliang) {
        Myutils.quyangliang = quyangliang;
    }

    public static String getQuyangtou() {
        return quyangtou;
    }

    public static void setQuyangtou(String quyangtou) {
        Myutils.quyangtou = quyangtou;
    }

    public static String getShengjiang() {
        return shengjiang;
    }

    public static void setShengjiang(String shengjiang) {
        Myutils.shengjiang = shengjiang;
    }

    public static void setPowername(String powername) {
        Myutils.powername = powername;
    }

    public static String getPowername() {
        return powername;
    }

    public static void setUsername(String username) {
        Myutils.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static void setSampleName(String sampleName) {
        Myutils.sampleName = sampleName;
    }

    public static String getSampleName() {
        return sampleName;
    }

    public static void setSampleNumbere(String sampleNumber) {
        Myutils.sampleNumber = sampleNumber;
    }

    public static String getSampleNumber() {
        return sampleNumber;
    }

    public static void setSampleType(String sampleType) {
        Myutils.sampleType = sampleType;
    }

    public static String getSampleType() {
        return sampleType;
    }

    private static int standard = 9;//

    public static void setStandard(int standard) {
        Myutils.standard = standard;
    }

    public static int getStandard() {
        return standard;
    }

    public static void setArrayList_1(ArrayList<String> arrayList_1) {
        Myutils.arrayList_1 = arrayList_1;
    }

    public static ArrayList<String> getArrayList_1() {
        return arrayList_1;
    }

    public static void setArrayList_2(ArrayList<String> arrayList_2) {
        Myutils.arrayList_2 = arrayList_2;
    }

    public static ArrayList<String> getArrayList_2() {
        return arrayList_2;
    }

    public static void setDanWei(String danWei) {
        Myutils.danWei = danWei;
    }

    public static String getDanWei() {
        return danWei;
    }

    public static void setCiShu(String ciShu) {
        Myutils.ciShu = ciShu;
    }

    public static String getCiShu() {
        return ciShu;
    }

    private static long fileSize = 0;

    public static long getFileSize() {
        return fileSize;
    }

    public static void setFileSize(long fileSize) {
        Myutils.fileSize = fileSize;
    }

    private static boolean startFlag=false;//开关机标志位

    public static boolean isStartFlag() {
        return startFlag;
    }

    public static void setStartFlag(boolean startFlag) {
        Myutils.startFlag = startFlag;
    }

    public static boolean handFlag=false;

    public static boolean isHandFlag() {
        return handFlag;
    }

    public static void setHandFlag(boolean handFlag) {
        Myutils.handFlag = handFlag;
    }
}
