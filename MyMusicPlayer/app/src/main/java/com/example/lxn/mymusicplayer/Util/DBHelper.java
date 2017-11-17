package com.example.lxn.mymusicplayer.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import com.example.lxn.mymusicplayer.Common.FileColumn;

/**
 * Created by lxn on 17-11-17.
 */

public class DBHelper extends SQLiteOpenHelper {
    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "MyMusic.db";
    /**
     * 数据库版本
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * 表名
     */
    public static final String TABLES_TABLE_NAME = "File_Table";
    private static final String DATABASE_CREATE = "CREATE TABLE " + FileColumn.TABLE +" ("
            + FileColumn.ID+" integer primary key autoincrement,"
            + FileColumn.NAME+" text,"
            + FileColumn.PATH+" text,"
            + FileColumn.SORT+" integer,"
            + FileColumn.TYPE+" text)";

    /**
     * 构造方法
     */
    public DBHelper(Context context){
        //创建数据库
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 创建是调用
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * 更新时调用
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //删除表
        db.execSQL("DROP TABLE IF EXISTS File_Table");
        onCreate(db);
    }
}
