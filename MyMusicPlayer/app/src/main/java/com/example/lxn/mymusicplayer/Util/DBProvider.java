package com.example.lxn.mymusicplayer.Util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Printer;

import com.example.lxn.mymusicplayer.Common.FileColumn;

/**
 * Created by lxn on 17-11-17.
 */

public class DBProvider extends ContentProvider{

    private final String TAG = "lxn***DBProvider";
    private DBHelper dbOpenHelper;
    public static final String AUTHORITY = "MUSIC";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FileColumn.TABLE);

    public int delete(Uri arg0, String arg1, String [] arg2){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        try {
            db.delete(FileColumn.TABLE, arg1,arg2);
            Log.d(TAG, "delete");
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "delete Exception");
        }
        return 1;
    }

    /**
     * 待实现
     */
    public String getType(Uri uri){
        return null;
    }

    /**
     * 插入
     */
    public Uri insert(Uri uri, ContentValues contentValues){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long count = 0;
        try {
            count = db.insert(FileColumn.TABLE, null, contentValues);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "insert_Exception");
        }
        if (count > 0){
            return uri;
        } else {
            return null;
        }
    }

    public boolean onCreate(){
        dbOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        //参数依次为： 表名， 查询字段， where 语句， 替换
        Cursor cur = db.query(FileColumn.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        return cur;
    }


    /**
     * 根据条件查询
     */
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int i = 0;
        try{
            i = db.update(FileColumn.TABLE, contentValues, selection, null);
            return i;
        }catch (Exception e){
            Log.e(TAG, "update_Exception");
        }
        return 0;
    }

}
