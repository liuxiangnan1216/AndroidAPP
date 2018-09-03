package com.example.agingmonkeytestv2;


import com.example.agingmonkeytestv2.testitem.AbstractBaseTestItem;
import com.example.agingmonkeytestv2.testitem.AudioTestItem;
import com.example.agingmonkeytestv2.testitem.BatteryTestItem;
import com.example.agingmonkeytestv2.testitem.BluetoothTestItem;
import com.example.agingmonkeytestv2.testitem.CameraPreviewTestItem;
import com.example.agingmonkeytestv2.testitem.CameraSubTestItem;
import com.example.agingmonkeytestv2.testitem.CameraSubVideoTestItem;
import com.example.agingmonkeytestv2.testitem.CameraTakePictureTestItem;
import com.example.agingmonkeytestv2.testitem.CameraTestItem;
import com.example.agingmonkeytestv2.testitem.CameraVideoTestItem;
import com.example.agingmonkeytestv2.testitem.FlashLightTestItem;
import com.example.agingmonkeytestv2.testitem.FocusMotorTestItem;
import com.example.agingmonkeytestv2.testitem.LcdTestItem;
import com.example.agingmonkeytestv2.testitem.SoundRecordTestItem;
import com.example.agingmonkeytestv2.testitem.WiFiTestItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/

public class BaseTestActivity extends Activity{

    public static final int MSG_COMPLETED = 3;
    public static final int MSG_SETUP = 5;
    public static final int MSG_SHOW_DISMISS_DIALOG = 9;
    public static final int MSG_START = 2;
    public static final int MSG_TEAR_DOWN = 7;
    public static final String TAG = "lxn-BaseTestActivity";
    public static final int TEST_TIMES = 10;
    public static final int TYPE_DURATION = 4;
    public static final int TYPE_LOOP = 2;
    public static final int TYPE_TIMES = 3;
    private boolean mFullScreen;

    private ProgressDialog mProgressDialog;
    private boolean mShowDialog;
    private boolean mStoped = false;
    private AbstractBaseTestItem mTestItem;
    private int mTestTimes = 10;
    private String mTestTitle;
    private int mTestType = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                	Log.d(TAG, "handleMessage: onTestStart");
                    BaseTestActivity.this.mTestItem.onTestStart();
                    break;
                case MSG_COMPLETED:
                	Log.d(TAG, "handleMessage: onTestCompleted");
                    BaseTestActivity.this.mTestItem.onTestCompleted();
                    break;
                case MSG_SETUP:
                    Log.d(TAG, "handleMessage: setup");
                    BaseTestActivity.this.mTestItem.setUp();
                    break;
                case MSG_TEAR_DOWN:
                	Log.d(TAG, "handleMessage: tearDown");
                    BaseTestActivity.this.mTestItem.tearDown();
                    break;
                case MSG_SHOW_DISMISS_DIALOG:
                	Log.d(TAG, "handleMessage: mShowDialog");
                    if (BaseTestActivity.this.mShowDialog) {
                        BaseTestActivity.this.dismissOrShowDialog();
                        break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.ALPHA_CHANGED);
        loadTestData(getIntent());
        if (this.mTestItem == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            finish();
        }
        RelativeLayout relativeLayout = new RelativeLayout(this);
        View view = this.mTestItem.getView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        if (view != null) {
            relativeLayout.addView(view, layoutParams);
        }
        if (this.mFullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(WindowManager.LayoutParams.CONTENTS_FILE_DESCRIPTOR);
            if (getActionBar() != null) {
                getActionBar().hide();
            }
        } else {
            setTitle(this.mTestTitle);
        }
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(0);
        setContentView((View) relativeLayout);
        this.mTestItem.onCreate();
    }


    public void loadTestData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.d(TAG, "when load test data, but get a null bundle.");
            finish();
            return;
        }
        this.mTestTitle = bundle.getString("testItemName");
        String className = bundle.getString("className");
        int resLayoutId = bundle.getInt("resLayoutId", -1);
        this.mFullScreen = bundle.getBoolean("fullScreen", false);
        this.mShowDialog = bundle.getBoolean("showDialog", false);
        this.mTestType = bundle.getInt("testType", 2);
        this.mTestTimes = bundle.getInt("testTimes", 10);
        Log.d(TAG, "\n\t testItemName: " + this.mTestTitle 
        		+ "\n\t  resLayoutId: " + resLayoutId 
        		+ "\n\t  className: " + className 
        		+ "\n\t  fullScreen: " + this.mFullScreen 
        		+ "\n\t  testType " + this.mTestType 
        		+ "\n\t  testTimes " + this.mTestTimes 
        		+ "\n\t  showDialog: " + this.mShowDialog);
        
        
        if (className.equals("com.example.agingmonkeytestv2.testitem.LcdTestItem")) {
        	LcdTestItem lcdTestItem = new LcdTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) lcdTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.WiFiTestItem")) {
        	WiFiTestItem wiFiTestItem = new WiFiTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) wiFiTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.BluetoothTestItem")) {
        	BluetoothTestItem buBluetoothTestItem = new BluetoothTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) buBluetoothTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.FlashLightTestItem")) {
        	FlashLightTestItem flashLightTestItem = new FlashLightTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) flashLightTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.CameraTestItem")) {
        	CameraTestItem cameraTestItem = new CameraTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) cameraTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.CameraSubTestItem")) {
        	CameraSubTestItem cameraSubTestItem = new CameraSubTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) cameraSubTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.CameraVideoTestItem")) {
        	CameraVideoTestItem cameraVideoTestItem = new CameraVideoTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) cameraVideoTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.CameraSubVideoTestItem")) {
        	CameraSubVideoTestItem cameraSubVideoTestItem = new CameraSubVideoTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) cameraSubVideoTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.CameraPreviewTestItem")) {
        	CameraPreviewTestItem cameraPreviewTestItem = new CameraPreviewTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) cameraPreviewTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.CameraTakePictureTestItem")) {
        	CameraTakePictureTestItem cameraTakePictureTestItem = new CameraTakePictureTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) cameraTakePictureTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.SoundRecordTestItem")) {
        	SoundRecordTestItem soundRecordTestItem = new SoundRecordTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) soundRecordTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.BatteryTestItem")) {
        	BatteryTestItem batteryTestItem = new BatteryTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) batteryTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.FocusMotorTestItem")) {
        	FocusMotorTestItem focusMotorTestItem = new FocusMotorTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) focusMotorTestItem;
        } else if (className.equals("com.example.agingmonkeytestv2.testitem.FocusMotorTestItem")) {
        	AudioTestItem audioTestItem = new AudioTestItem(resLayoutId);
        	this.mTestItem = (AbstractBaseTestItem) audioTestItem;
        }
        Log.d(TAG,"loadTestData this.mTestItem==="+this.mTestItem);
//        try {
//        	
//        	Class clazz = Class.forName(className);
//        	Constructor constructor = clazz.getDeclaredConstructor(new Class[]{int.class});
//        	Object obj = constructor.newInstance(new Object[]{Integer.valueOf(resLayoutId)});
//        	
//           // Object obj = Class.forName(className).getDeclaredConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(resLayoutId)});
//            Log.d(TAG,"loadTestData obj==="
//            		+"\n\t Class=="+ new Class[]{Integer.TYPE}
//            		+"\n\t Integer.valueOf==" + new Object[]{Integer.valueOf(resLayoutId)}
//            		+"\n\t obj==" + obj
//            		);
//            
//            
//            if (obj instanceof AbstractBaseTestItem) {
//                this.mTestItem = (AbstractBaseTestItem) obj;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(TAG, "loadTestData: " + e.toString());
//        }
    }


    protected void onResume() {
        Log.d(TAG, "test activity onResume");
        super.onResume();
        this.mStoped = false;
        this.mTestItem.onResume();
        if (this.mTestType == 2) {
            this.mTestItem.test(this.mHandler);
        } else if (this.mTestType == 4) {
            this.mTestItem.testByDuration(this.mHandler);
        } else if (this.mTestType == 3) {
            this.mTestItem.testByTimes(this.mHandler, this.mTestTimes);
        } else {
            throw new RuntimeException("Error, test type " + this.mTestType + " not permitted");
        }
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Test Item Stopped by activity paused " + this.mTestTitle);
        this.mStoped = true;
        this.mTestItem.onPause();
    }

    protected void onStop() {
        super.onStop();
        this.mTestItem.onStop();
    }

    protected void onRestart() {
        super.onRestart();
        this.mTestItem.onRestart();
    }

    protected void onDestroy() {
        this.mTestItem.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.mTestItem.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void dismissOrShowDialog() {
        if (!this.mStoped) {
            if (this.mProgressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            } else {
                this.mProgressDialog.show();
            }
        }
    }
}
