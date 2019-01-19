package com.example.lxn.agingtestnew.testItem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.MemoryFile;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class MemoryTestItem extends AbstractBaseTestItem {
    private static final String TAG = "lxn-MemoryTestItem";

    private Context mContext;
    private MemoryFile mMemoryFile;
    private final int MEMORY_SIZE = 128 * 1024;
    private byte[] buffer_write;
    private byte[] buffer_read;
    private int testTimes = 0;
    private int showTimes = 1;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private Bitmap bmp;
    private ByteArrayOutputStream baos;


    public MemoryTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        if (showTimes <= testTimes) {
            boolean isOK = writeReadJPG();
            if (!isOK) {
                tv3.setText(String.format("第 %s 次数据写入读取不一致", showTimes));
                tv3.setTextColor(Color.RED);
                onStop();
                onTestEnd();
                CompletedActivity.memoryStatus ="FAIL";
                return true;
            } else {
                tv2.setText(String.format("第 %s 次数据写入读取一致", showTimes));
            }
            showTimes++;
        } else {
//            onStop();
            onTestEnd();
            if (testTimes != 0) {
                CompletedActivity.memoryStatus = "PASS";
            }
            return true;
        }
        return false;
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        mContext = view.getContext();
        tv1 = (TextView) view.findViewById(R.id.tv_memory1);
        tv2 = (TextView) view.findViewById(R.id.tv_memory2);
        tv3 = (TextView) view.findViewById(R.id.tv_memory3);
        testTimes = SettingsActivity.memory_times;

        Log.d(TAG, "initView test times====" + testTimes);
        try {
            mMemoryFile = new MemoryFile(null, MEMORY_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Resources resources = mContext.getResources();
        bmp = BitmapFactory.decodeResource(resources, R.drawable.testmemory);
        baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        buffer_write = baos.toByteArray();

    }

    public boolean writeReadJPG() {
        Log.d(TAG, "writeJPG");
        tv1.setText("数据写入读取中。。。");
        try {
            mMemoryFile.writeBytes(buffer_write, 0, 0, MEMORY_SIZE);
            buffer_read = new byte[buffer_write.length];
            mMemoryFile.readBytes(buffer_read, 0, 0, MEMORY_SIZE);
            boolean isSame = compareBuffers(buffer_write, buffer_read);
            return isSame;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//
//
//    public byte[] readMemory() {
//        Log.d(TAG, "readMemory");
//        SystemClock.sleep(50);
//        tv1.setText("数据读取中。。。");
//        buffer_read = new byte[buffer_write.length];
//        try {
//            mMemoryFile.readBytes(buffer_read, 0, 0, MEMORY_SIZE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        Log.d(TAG, "to string ====" + Arrays.toString(buffer_read));
//        return buffer_read;
//    }



    public boolean compareBuffers (byte[] bytes1, byte[] bytes2) {
//        SystemClock.sleep(50);
        tv1.setText(String.format("第 %s 次数据比对中。。。", showTimes));
        Log.d(TAG, "compareBuffers");
        byte[] by1 = new byte[128 * 1024];
        byte[] by2 = new byte[128 * 1024];
        System.arraycopy(bytes1, 0, by1, 0, MEMORY_SIZE);
        System.arraycopy(bytes2, 0, by2, 0, MEMORY_SIZE);

        if (Arrays.equals(by1, by2)) {
            return true;
        } else {
            return false;
        }

//            for (int i = 0; i < MEMORY_SIZE; i++) {
//                if (bytes1[i] != bytes2[i]) {
////                    Log.d(TAG, "\n\ni======" + i + "\nbytes 1===" + bytes1[i] + "\n bytes 2===" + bytes2[i]);
//                    return false;
//                }
//            }
//            return true;
//        } else {
//            return false;
//        }
    }
}
