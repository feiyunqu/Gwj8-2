package com.tdtf.gwj8.tools;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.R;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/2/8.
 * <p>
 * 发给机器的命令
 */

public class MyReceive extends BroadcastReceiver {

    private final String TAG = "MyReceive";
    File file = new File("/data/data/com.tdtf.client/databases/weili16.db");
    Handler handler;

    @Override
    public void onReceive(final Context context, Intent intent) {
//        Intent.ACTION_MEDIA_SHARED//如果SDCard未安装,并通过USB大容量存储共享返回
//        Intent.ACTION_MEDIA_MOUNTED//表明sd对象是存在并具有读/写权限
//        Intent.ACTION_MEDIA_UNMOUNTED//SDCard已卸掉,如果SDCard是存在但没有被安装
//        Intent.ACTION_MEDIA_CHECKING  //表明对象正在磁盘检查
//        Intent.ACTION_MEDIA_EJECT  //物理的拔出 SDCARD
//        Intent.ACTION_MEDIA_REMOVED  //完
//        if (intent.getAction().equals("Android.intent.action.MEDIA_EJECT")
//                || intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED")) {
//            Log.d(TAG, "onReceive: 1"+intent.getDataString());
//
//
//        }else
        if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
            String path = intent.getDataString();
            final String pathString = path.split("file://")[1];
            Log.d(TAG, "onReceive: " + pathString);

            if (Myutils.getPowername().equals("超级用户")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("提示").setIcon(R.drawable.usb)
                        .setMessage("检测到U盘插入，是否导出记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long diskSpace = DiskTools.getExternalDevices(pathString, 1);//U盘剩余空间
                                long fileSize = 0;
                                try {
                                    fileSize = getFileSize(file);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Myutils.setFileSize(fileSize);
                                if (fileSize >= diskSpace) {
                                    usbDialog(context, "警告", "U盘空间不足，无法导出记录！");
                                } else {
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    LinearLayout waiting = (LinearLayout) inflater.inflate(R.layout.waiting, null);
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context)
                                            .setTitle("正在导出文件")
                                            .setIcon(R.drawable.warning)
                                            .setView(waiting);
                                    builder1.create();
                                    final AlertDialog alertDialog = builder1.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                byte[] bs = new byte[2048];
                                                int len;
                                                //String strPath=File.separator+"data"+File.separator+"data"+File.separator+"databases"+File.separator+"com.tdtf.client"+File.separator+"weili16.db";
                                                String strPath = "/data/data/com.tdtf.client/databases/weili16.db";
                                                File tempFile = new File(strPath);
                                                if (!tempFile.exists()) {
                                                    tempFile.mkdirs();
                                                }

                                                long fileSize0 = 0;
                                                try {
                                                    fileSize0 = getFileSize(file);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                int a = (int) (Math.random() * 999999999);
                                                FileInputStream ins = new FileInputStream(strPath);
                                                FileOutputStream os = new FileOutputStream(pathString + File.separator + "weili" + a + ".db");
                                                while ((len = ins.read(bs)) != -1) {
                                                    os.write(bs, 0, len);
                                                    os.flush();
                                                    File cFile = new File(pathString + File.separator + "weili" + a + ".db");
                                                    long fileCopy = 0;
                                                    try {
                                                        fileCopy = getFileSize(cFile);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    if (fileSize0 == fileCopy) {
                                                        if (cFile.renameTo(new File(pathString + File.separator + "weili16.db"))) {
                                                            Message msg = Message.obtain();
                                                            msg.what = 0;
                                                            msg.obj = context;
                                                            ins.close();
                                                            os.close();
                                                            //数据库操作
                                                            clearData(context);
                                                            ///////////

                                                            alertDialog.dismiss();
                                                            handler.sendMessageDelayed(msg, 0);
                                                        }
                                                    }
//                                                    else {
//                                                        Message msg = Message.obtain();
//                                                        msg.what = 1;
//                                                        msg.obj = context;
//                                                        handler.sendMessageDelayed(msg, 0);
//                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            } else {
                usbDialog(context, "警告", "用户权限不足，无法导出记录！");
            }
        }

        if (intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("提示").setIcon(R.drawable.usb)
                    .setMessage("U盘已经拔出")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            long oddFileSize = Myutils.getFileSize();
                            long newFileSize = 0;
                            File nFile = new File("/data/data/com.tdtf.client/databases/weili16.db");
                            try {
                                newFileSize = getFileSize(nFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (oddFileSize == newFileSize) {
                                usbDialog(context, "提示", "文件导出失败");
                            }
                        }
                    });
            builder.create().show();
        }

        handler = new

                Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 0:
                                Context context1 = (Context) msg.obj;
                                usbDialog(context1, "提示", "文件导出成功");
                                break;
                            case 1:
                                Context context2 = (Context) msg.obj;
                                usbDialog(context2, "提示", "文件导出失败");
                                break;
                        }
                    }
                }

        ;
    }

    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    public void usbDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title).setIcon(R.drawable.usb)
                .setIcon(R.drawable.warning)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    public void clearData(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        String[] sqlstr = new String[8];
        sqlstr[0] = "Data";
        sqlstr[1] = "DataMazui";
        sqlstr[2] = "DataLvchu";
        sqlstr[3] = "DataWuran";
        sqlstr[4] = "DataWR";
        sqlstr[5] = "DataYaodian";
        sqlstr[6] = "Dataname";
        sqlstr[7] = "Diary";

        dbHelper.getReadableDatabase().execSQL("delete from Data");
        dbHelper.getReadableDatabase().execSQL("delete from DataMazui");
        dbHelper.getReadableDatabase().execSQL("delete from DataLvchu");
        dbHelper.getReadableDatabase().execSQL("delete from DataWuran");
        dbHelper.getReadableDatabase().execSQL("delete from DataWR");
        dbHelper.getReadableDatabase().execSQL("delete from DataYaodian");
        dbHelper.getReadableDatabase().execSQL("delete from Dataname");
        dbHelper.getReadableDatabase().execSQL("delete from Diary");
        for (int i = 0; i < 8; i++) {
            dbHelper.getReadableDatabase().execSQL(
                    "update sqlite_sequence set seq=0 where name =?", new String[]{sqlstr[i]});
        }
    }
}
