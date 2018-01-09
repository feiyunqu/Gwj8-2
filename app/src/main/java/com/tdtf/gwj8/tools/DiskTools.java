package com.tdtf.gwj8.tools;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by lenovo on 2017/8/15.
 */

public class DiskTools {

    /**
     * 获取sd卡剩余空间
     *
     * @param a 输入0、1、2，返回不同的数据，单位MB
     *          a=0  返回总大小
     *          a=1  返回剩余大小
     *          a=2  返回已用大小
     */
    public static int getSDCardSpace(int a) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
//            Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
//            Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");
            if (a == 0) {
                return (int) (blockSize * blockCount / (1024 * 1024));
            }
            if (a == 1) {
                return (int) (availCount * blockSize / (1024 * 1024));
            }
            if (a == 2) {
                return (int) (blockSize * blockCount / (1024 * 1024) - availCount * blockSize / (1024 * 1024));
            }
            return -1;
        }
        return -1;
    }

    /**
     * 获取本机剩余空间
     *
     * @param a 输入0、1、2，返回不同的数据，单位MB
     *          a=0  返回总大小
     *          a=1  返回剩余大小
     *          a=2  返回已用大小
     */
    public static int getSystemDiskSpace(int a) {
        File root = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
//        Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
//        Log.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
        if (a == 0) {
            return (int) (blockSize * blockCount / (1024 * 1024));
        }
        if (a == 1) {
            return (int) (availCount * blockSize / (1024 * 1024));
        }
        if (a == 2) {
            return (int) (blockSize * blockCount / (1024 * 1024) - availCount * blockSize / (1024 * 1024));
        }
        return -1;
    }

    /**
     * 获取外接设备剩余空间(kb)
     *
     * @param a 输入0、1、2，返回不同的数据，单位MB
     *          a=0  返回总大小
     *          a=1  返回剩余大小
     *          a=2  返回已用大小
     */
    public static long getExternalDevices(String state, int a) {
        File sdcardDir = new File(state);
        StatFs sf = new StatFs(sdcardDir.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
//            Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
//            Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");
        if (a == 0) {
            return (blockSize * blockCount / 1024);
        }
        if (a == 1) {
            return (availCount * blockSize / 1024);
        }
        if (a == 2) {
            return (blockSize * blockCount / 1024 - availCount * blockSize / 1024);
        }
        return -1;
    }
}
