package com.tdtf.gwj8;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.tdtf.gwj8.api.SerialPort;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;
import com.tdtf.gwj8.myDataBase.MyDiary;
import com.tdtf.gwj8.tools.MyReceive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyService extends Service {
    FileOutputStream mOutputStream;
    FileInputStream mInputStream;
    SerialPort sp;
    Thread thread;
    StringBuffer stringBuffer = new StringBuffer();
    Handler handler = new Handler();
    CallBacks callbacks;
    boolean flag = true;
    DataReceived dataReceived = new DataReceived();
    MyDatabaseHelper dbHelper;
    MyDiary myDiary = new MyDiary();
    ArrayList<String> biaozhi = new ArrayList<>();
    String streamdata = "";

    /*TODO 读取串口的数据处理*/
    private class DataReceived implements Runnable {
        @Override
        public void run() {
            if (stringBuffer.length() != 0) {
                String msg = stringBuffer.toString();
                stringBuffer.delete(0, stringBuffer.length());
                Log.d("QWER", "run: " + msg);
                streamdata = streamdata + msg;
                Log.d("dddd", "run: " + streamdata);
                if (streamdata.startsWith("aa0001") || streamdata.startsWith("ff") || streamdata.startsWith("aa0002")) {
                    streamdata = "";
                }
                if (streamdata.startsWith("aa0004") && streamdata.length() > 28) {
                    streamdata = "";
                }
                if (streamdata.length() > 5) {
                    if (msg.startsWith(SerialOrder.ORDER_HANDSHAKING)) {
                        if (msg.startsWith(SerialOrder.ORDER_HANDSHAKING)) {//握手
                            try {
                                for (int i = 0; i < 14; i = i + 2) {
                                    mOutputStream.write(Integer.parseInt(SerialOrder.ORDER_HANDSHAKING.substring(i, i + 2), 16));
                                }
                                Log.d("wwww", "run: 00get");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (callbacks != null) {
                                callbacks.startRead();
                            }
                        }
                        streamdata = "";
                    }

                    if (streamdata.startsWith("aa0004") && streamdata.endsWith("cc33c33c") && streamdata.length() < 29) {//检测设置
                        String[] strJianCeSheZhi = SolveFunction.upJianCeSheZhi(streamdata);
                        for (int m = 0; m < strJianCeSheZhi.length; m++) {
                            myDiary.diary(dbHelper, strJianCeSheZhi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa0005") && streamdata.endsWith("cc33c33c")) {//清洗设置
                        String[] strQingXiSheZhi = SolveFunction.upQingXiSheZhi(streamdata);
                        for (int m = 0; m < strQingXiSheZhi.length; m++) {
                            myDiary.diary(dbHelper, strQingXiSheZhi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa0006") && streamdata.endsWith("cc33c33c")) {//搅拌速度
                        String[] strJiaoBanSuDu = SolveFunction.upJiaoBanSuDu(streamdata);
                        for (int m = 0; m < strJiaoBanSuDu.length; m++) {
                            myDiary.diary(dbHelper, strJiaoBanSuDu[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa0007") && streamdata.endsWith("cc33c33c")) {//通道设置
                        String[] strTongDaoSheZhi = SolveFunction.upTongDaoSheZhi(streamdata);

                        setSharedPreference("自定义一", Myutils.getArrayList_1());
                        setSharedPreference("自定义二", Myutils.getArrayList_2());

                        Log.d("kkk", "run: " + Myutils.getStandard());

                        for (int m = 0; m < strTongDaoSheZhi.length; m++) {
                            myDiary.diary(dbHelper, strTongDaoSheZhi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa0008") && streamdata.endsWith("cc33c33c")) {//自检信息
                        String[] strZiJianXinXi = SolveFunction.upZiJianXinXi(streamdata);
                        for (int m = 0; m < strZiJianXinXi.length; m++) {
                            myDiary.diary(dbHelper, strZiJianXinXi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa0009") && streamdata.endsWith("cc33c33c")) {//打印设置
                        String[] strDaYinSheZhi = SolveFunction.upDaYinSheZhi(streamdata);
                        for (int m = 0; m < strDaYinSheZhi.length; m++) {
                            myDiary.diary(dbHelper, strDaYinSheZhi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa000a") && streamdata.endsWith("cc33c33c")) {//标尺
                        String[] strBiaoChi = SolveFunction.upBiaoChi(streamdata);
                        for (int m = 0; m < strBiaoChi.length; m++) {
                            myDiary.diary(dbHelper, strBiaoChi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa000b") && streamdata.endsWith("cc33c33c")) {//速度设置
                        String[] strSuDuSheZhi = SolveFunction.upSuDuSheZhi(streamdata);
                        for (int m = 0; m < strSuDuSheZhi.length; m++) {
                            myDiary.diary(dbHelper, strSuDuSheZhi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa000c") && streamdata.endsWith("cc33c33c")) {//噪声测定
                        String[] strZaoShengCeDing = SolveFunction.upZaoShengCeDing(streamdata);
                        for (int m = 0; m < strZaoShengCeDing.length; m++) {
                            myDiary.diary(dbHelper, strZaoShengCeDing[m]);
                        }
                        streamdata = "";
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa000d") && streamdata.endsWith("cc33c33c")) {//修正值
                        String[] strXiuZhengZhi = SolveFunction.upXiuZhengZhi(streamdata);
                        for (int m = 0; m < strXiuZhengZhi.length; m++) {
                            myDiary.diary(dbHelper, strXiuZhengZhi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa000e") && streamdata.endsWith("cc33c33c")) {//样品信息
                        String[] strYangPinXinXi = SolveFunction.upYangPinXinXi(streamdata);
                        for (int m = 0; m < strYangPinXinXi.length; m++) {
                            myDiary.diary(dbHelper, strYangPinXinXi[m]);
                        }
                        if (callbacks != null) {
                            callbacks.startRead();
                        }
                        streamdata = "";
                    }
                    if (streamdata.startsWith("aa000f") && streamdata.endsWith("cc33c33c")) {//检测数据
                        SolveFunction.upJianCeShuJu(dbHelper, biaozhi, streamdata);
                        streamdata = "";
                    }
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new MyDatabaseHelper(this);
        biaozhi.add("a");
        biaozhi.add("b");
        biaozhi.add("c");
        biaozhi.add("d");
        biaozhi.add("e");
        biaozhi.add("f");
        biaozhi.add("g");
        biaozhi.add("h");
        biaozhi.add("i");
        biaozhi.add("j");
        biaozhi.add("k");
        biaozhi.add("l");
        biaozhi.add("z");//用于数据库标志分类
        Log.d("tag", "onCreate: " + biaozhi.size());
        /*TODO 打开串口*/
        try {
            //sp = new SerialPort(new File("/dev/ttyAMA0"), 9600, 0);
            sp = new SerialPort(new File("/dev/ttyS2"), 9600, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mOutputStream = (FileOutputStream) sp.getOutputStream();
        mInputStream = (FileInputStream) sp.getInputStream();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mInputStream != null) {
                    try {
                        int length = mInputStream.available();
                        if (length > 0) {
                            byte[] buffer = new byte[length];
                            mInputStream.read(buffer);//该方法会阻塞线程直到接收到数据
                            stringBuffer.append(Transform.byte2hex(buffer));
                            handler.post(dataReceived);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("tag", "onUnbind: ");
        flag = false;
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("tag", "onRebind: ");
//        if (threadPress.isInterrupted())
//            thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ffff", "onDestroy: ");
        mInputStream = null;

        flag = false;
        sp.close();
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }

        public void threadGo() {
            flag = true;
        }
    }

    public interface CallBacks {
        void startRead();

        void output(FileOutputStream outputStream);
    }

    public void setValues(CallBacks callBacks) {
        this.callbacks = callBacks;
        if (callBacks != null)
            callbacks.output(mOutputStream);
    }

    public void setSharedPreference(String key, ArrayList<String> values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences coefficient;

        coefficient = getSharedPreferences("arrayArgument", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        if (values != null && values.size() > 0) {
            for (int k = 0; k < values.size(); k++) {
                str += values.get(k);
                str += regularEx;
            }
            editor = coefficient.edit();
            editor.putString(key, str);
            editor.putInt("standard", Myutils.getStandard());
            editor.putString("danwei", Myutils.getDanWei());
            editor.putString("cishu", Myutils.getCiShu());
            editor.putString("shengjiang",Myutils.getShengjiang());
            editor.putString("jiance",Myutils.getJiance());
            editor.putString("yuzouliang",Myutils.getYuzouliang());
            editor.putString("quyangliang",Myutils.getQuyangliang());
            editor.apply();
        }
    }
}
