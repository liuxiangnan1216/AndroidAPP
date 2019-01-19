package com.example.lxn.agingtestnew.testItem;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class EmmcTestItem extends AbstractBaseTestItem {

    private static final String TAG = "lxn-EmmcTestItem";

    private Context mContext;
    private FileInputStream fis;
    private TextView tv_info;
    private TextView tv_times;
    private File file_path;
    private File file;

    private int testTimes = 0;
    private int times = 1;

    public EmmcTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        if (times <= testTimes) {
            tv_times.setText(String.format("正在进行第 %s 次测试", times));
            byte[] writeByte = readFile();
            writeFile(writeByte);
            times++;
            return false;
        } else {
            SystemClock.sleep(200);
            tv_info.setText("测试完成！");
            tv_times.setText(String.format("测试完成！共计测试 %s 次", (times - 1)));
            if (testTimes != 0) {
                CompletedActivity.emmcStatus = "PASS";
            }
//            onStop();
            onTestEnd();
            return true;
        }
    }

    @Override
    public void onTestEnd() {
        super.onTestEnd();
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        mContext = view.getContext();
        tv_info = (TextView) view.findViewById(R.id.tv_emmcinfo);
        tv_times = (TextView) view.findViewById(R.id.tv_emmctimes);

        file_path = Environment.getExternalStorageDirectory();
        file = new File(file_path.getAbsoluteFile(), "emmc_i.mp3");
        testTimes = SettingsActivity.emmc_times;
        Log.d(TAG, "initView test times====" + testTimes);

    }


    public byte[] readFile() {
        SystemClock.sleep(200);
        InputStream in = mContext.getResources().openRawResource(R.raw.audio);
        byte[] buffer = new byte[1024 * 1024];
        tv_info.setText("正在读取文件。。。");
        try {
            in.read(buffer);
            Log.d(TAG, "buffer===" + buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public void writeFile(byte[] buffer) {
        SystemClock.sleep(200);
//        File file_path = Environment.getExternalStorageDirectory();
//        File file = new File(file_path.getAbsoluteFile(), "emmc_i.mp3");
        Log.d(TAG, "file path===" + file.getAbsolutePath());
        tv_info.setText("正在写入文件emmc_i.mp3中。。。");
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(buffer, 0, buffer.length);
            fos.flush();
            long size1 = getSize(file);
            tv_info.setText("文件size:  " + size1 / (1024 * 1024) + "M");
            fos.write(buffer, 0, buffer.length);
            fos.flush();
            Long size2 = getSize(file);
            fos.close();
            if (size2 > size1) {
                Log.d(TAG, "size 可以写入");
                tv_info.setText("文件size:  " + size1 / (1024 * 1024) + "M，可以继续写入。。。");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSize(file);
        deleteFile(file.getAbsolutePath());
    }


    public long getSize(File file) {
        long size = 0;
        try {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "file size=====" + size);
        return size;
    }

    public void deleteFile(String filePath) {
        SystemClock.sleep(200);
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            tv_info.setText("删除文件！");
        }

    }
}
