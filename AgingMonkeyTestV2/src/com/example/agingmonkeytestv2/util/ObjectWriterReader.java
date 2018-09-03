package com.example.agingmonkeytestv2.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import com.example.agingmonkeytestv2.testitem.TestItemHolder;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-9.
 ************************************************************************/


public class ObjectWriterReader {
    public static final String NAME = "ObjectSaver";
    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String TAG = "ObjectWriterReader";

    public static void write(Bundle bundle) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bout);
            ArrayList<TestItemHolder> mHolderArrayList = bundle.getParcelableArrayList("ItemHolderList");
            ArrayList<String> holdersList = new ArrayList();
            Iterator it = mHolderArrayList.iterator();
            while (it.hasNext()) {
                holdersList.add(((TestItemHolder) it.next()).createJSONObject().toString());
            }
            int mTestType = bundle.getInt("TestType", -1);
            int mTestTimes = bundle.getInt("TestTimes", -1);
            int mRebootTimes = bundle.getInt("RebootTimes", -1);
            long mTestDuration = bundle.getLong("TestDuration", -1);
            boolean mTestNeedReboot = bundle.getBoolean("TestNeedReboot", false);
            objectOutputStream.writeObject(holdersList);
            objectOutputStream.writeObject(Integer.valueOf(mTestType));
            objectOutputStream.writeObject(Integer.valueOf(mTestTimes));
            objectOutputStream.writeObject(Integer.valueOf(mRebootTimes));
            objectOutputStream.writeObject(Long.valueOf(mTestDuration));
            objectOutputStream.writeObject(Boolean.valueOf(mTestNeedReboot));
            objectOutputStream.flush();
            objectOutputStream.close();
            bout.close();
            byte[] b = bout.toByteArray();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(PATH, NAME));
            fileOutputStream.write(b);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public static Bundle read() {
        try {
            FileInputStream inputStream = new FileInputStream(new File(PATH, NAME));
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ArrayList<TestItemHolder> mHolderArrayList = new ArrayList<TestItemHolder>();//TODO
            Iterator iterator = ((ArrayList)objectInputStream.readObject()).iterator();

            while (iterator.hasNext()) {
                mHolderArrayList.add(TestItemHolder.copyFromJsonObject(new JSONObject((String) iterator.next())));
            }

            int mTestType = ((Integer) objectInputStream.readObject()).intValue();
            int mTestTimes = ((Integer) objectInputStream.readObject()).intValue();
            int mRebootTimes = ((Integer) objectInputStream.readObject()).intValue();
            long mTestDuration = ((Integer) objectInputStream.readObject()).longValue();
            boolean mTestNeedReboot = ((Boolean) objectInputStream.readObject()).booleanValue();

            inputStream.close();
            objectInputStream.close();

            Bundle bundle = new Bundle();
            bundle.putInt("TestType", mTestType);
            bundle.putInt("TestTimes", mTestTimes);
            bundle.putInt("RebootTimes", mRebootTimes);
            bundle.putLong("TestDuration", mTestDuration);
            bundle.putBoolean("TestNeedReboot", mTestNeedReboot);
            bundle.putParcelableArrayList("ItemHolderList", mHolderArrayList);

            return bundle;
        } catch (Exception e) {

            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static void clear() {
        Log.d(TAG, "clear delete the config file");
        new File(PATH, NAME).deleteOnExit();
    }
}
