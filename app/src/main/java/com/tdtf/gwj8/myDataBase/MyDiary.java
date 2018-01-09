package com.tdtf.gwj8.myDataBase;

import com.tdtf.gwj8.Myutils;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;

/**
 * Created by a on 2017/4/13.
 * 向日志库中写入日志
 */

public class MyDiary {
    public void diary(MyDatabaseHelper myDatabaseHelper, String context) {
        myDatabaseHelper.getReadableDatabase().execSQL(
                "insert into Diary values(null,?,?,?,?)",
                new String[]{
                        Myutils.formatDateTime(System.currentTimeMillis()),
                        context,
                        Myutils.getPowername(),
                        Myutils.getUsername()
                });
    }
}
