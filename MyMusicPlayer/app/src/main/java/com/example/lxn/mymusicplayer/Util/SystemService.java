package com.example.lxn.mymusicplayer.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Xml;

/**
 * Created by lxn on 17-11-17.
 */

public class SystemService {
    private Context context;
    private Cursor cursor;
    public SystemService(Context context){
        this.context = context;
    }

    public Cursor allSongs(){
        if (cursor != null){
            return cursor;
        }
        ContentResolver resolver = context.getContentResolver();
        cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        return cursor;
    }

    /**
     * 读取正在播放歌曲的艺术家
     */
    public String getArtist(){
        return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
    }

    /**
     * 读取正在播放歌曲的名字
     */
    public String getTitle(){
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        try{
            title = new String(title.getBytes(), "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return title;
    }

    /**
     * 读取正在播放的歌曲的专辑
     */
    public String getAlbum()throws RemoteException{
        return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
    }
}
