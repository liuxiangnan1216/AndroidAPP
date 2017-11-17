package com.example.lxn.mymusicplayer.Common;

/**
 * Created by lxn on 17-11-17.
 */

public class FileColumn {
    public static final String TABLE = "File_Table";

    public static final String ID = "fileID";
    public static final String NAME = "fileName";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String SORT = "sortPId";

    public static final int ID_COLUMN = 0;
    public static final int NAME_COLUMN = 1;
    public static final int PATH_COLUMN = 2;
    public static final int TYPE_COLUMN = 3;
    public static final int SORT_COlUMN = 4;
    /**
     * »ñÈ¡ÁÐÃû
     * @return ÁÐÃûÊý×é
     */
    public static String[] getColumArray(){
        return new String[]{ID,NAME,PATH,TYPE,SORT};
    }
}
