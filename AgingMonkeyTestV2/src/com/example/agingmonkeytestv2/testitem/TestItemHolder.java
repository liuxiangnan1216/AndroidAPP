package com.example.agingmonkeytestv2.testitem;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-9.
 ************************************************************************/


public class TestItemHolder implements Parcelable {
	
	private boolean mIsChecked = false;
	
    public static final Creator<TestItemHolder> CREATOR = new Creator<TestItemHolder>() {
        public TestItemHolder createFromParcel(Parcel in) {
            return new TestItemHolder(in);
        }

        public TestItemHolder[] newArray(int size) {
            return new TestItemHolder[size];
        }
    };
    private String className;
    private boolean isFullScreen;
    private boolean isShowDialog;
    private String itemName;
    private int resLayoutId;
    private int times;
    private int type;

    private TestItemHolder() {
    }
    
    public boolean isChecked() {
        return mIsChecked;
    }
    
    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public TestItemHolder(String name, int id, String testClassName, boolean fullScreen, boolean showDialog) {
        this(name, id, testClassName, 2, 10, fullScreen, showDialog);
    }

    public TestItemHolder(String name, int id, String testClassName, int testType, int testTimes, boolean fullScreen, boolean showDialog) {
        this.itemName = name;
        this.resLayoutId = id;
        this.className = testClassName;
        this.type = testType;
        this.times = testTimes;
        this.isFullScreen = fullScreen;
        this.isShowDialog = showDialog;
    }

    protected TestItemHolder(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.itemName = in.readString();
        this.resLayoutId = in.readInt();
        this.className = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isFullScreen = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.isShowDialog = z2;
        this.type = in.readInt();
        this.times = in.readInt();
    }

    public static TestItemHolder copyFromJsonObject(JSONObject jsonObject) throws JSONException {
        return new TestItemHolder(jsonObject.getString("testItemName"), jsonObject.getInt("resLayoutId"), jsonObject.getString("className"), jsonObject.getInt("testType"), jsonObject.getInt("testTimes"), jsonObject.getBoolean("fullScreen"), jsonObject.getBoolean("showDialog"));
    }

    public Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("testItemName", this.itemName);
        bundle.putInt("resLayoutId", this.resLayoutId);
        bundle.putString("className", this.className);
        bundle.putBoolean("fullScreen", this.isFullScreen);
        bundle.putBoolean("showDialog", this.isShowDialog);
        bundle.putInt("testType", this.type);
        bundle.putInt("testTimes", this.times);
        return bundle;
    }

    public JSONObject createJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("testItemName", this.itemName);
        jsonObject.put("resLayoutId", this.resLayoutId);
        jsonObject.put("className", this.className);
        jsonObject.put("fullScreen", this.isFullScreen);
        jsonObject.put("showDialog", this.isShowDialog);
        jsonObject.put("testType", this.type);
        jsonObject.put("testTimes", this.times);
        return jsonObject;
    }

    public String getItemName() {
        return this.itemName;
    }

    public int getResLayoutId() {
        return this.resLayoutId;
    }

    public void setType(int testType) {
        this.type = testType;
    }

    public void setTimes(int testTimes) {
        this.times = testTimes;
    }

    public String getClassName() {
        return this.className;
    }

    public boolean isFullScreen() {
        return this.isFullScreen;
    }

    public boolean isShowDialog() {
        return this.isShowDialog;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.itemName);
        dest.writeInt(this.resLayoutId);
        dest.writeString(this.className);
        if (this.isFullScreen) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.isShowDialog) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        dest.writeInt(this.type);
        dest.writeInt(this.times);
    }
}

