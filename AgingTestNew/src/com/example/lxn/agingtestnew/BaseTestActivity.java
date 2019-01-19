package com.example.lxn.agingtestnew;

import android.annotation.SuppressLint;
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

import com.example.lxn.agingtestnew.testItem.AbstractBaseTestItem;
import com.example.lxn.agingtestnew.testItem.AudioTestItem;
import com.example.lxn.agingtestnew.testItem.BackLightTestItem;
import com.example.lxn.agingtestnew.testItem.BatteryTestItem;
import com.example.lxn.agingtestnew.testItem.BluetoothTestItem;
import com.example.lxn.agingtestnew.testItem.CameraTestItem;
import com.example.lxn.agingtestnew.testItem.ChargingTestItem;
import com.example.lxn.agingtestnew.testItem.EarpieceTestitem;
import com.example.lxn.agingtestnew.testItem.EmmcTestItem;
import com.example.lxn.agingtestnew.testItem.GpsTestItem;
import com.example.lxn.agingtestnew.testItem.GraTestItem;
import com.example.lxn.agingtestnew.testItem.LcdTestItem;
import com.example.lxn.agingtestnew.testItem.LightTestItem;
import com.example.lxn.agingtestnew.testItem.MemoryTestItem;
import com.example.lxn.agingtestnew.testItem.RebootTestItem;
import com.example.lxn.agingtestnew.testItem.SensorTestItem;
import com.example.lxn.agingtestnew.testItem.VibratorTestItem;
import com.example.lxn.agingtestnew.testItem.VideoTestItem;
import com.example.lxn.agingtestnew.testItem.WifiTestItem;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 19-1-5.
 ************************************************************************/


public class BaseTestActivity extends Activity {

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
    private boolean mIsStartNextTest = false;
    private String mTestTitle;
    private int mTestType = 2;




    @SuppressLint("HandlerLeak")
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
        this.mIsStartNextTest = bundle.getBoolean("isStartNextTest", false);
        Log.d(TAG, "\n\t testItemName: " + this.mTestTitle
            + "\n\t  resLayoutId: " + resLayoutId
            + "\n\t  className: " + className
            + "\n\t  fullScreen: " + this.mFullScreen
            + "\n\t  testType " + this.mTestType
            + "\n\t  testTimes " + this.mTestTimes
            + "\n\t  showDialog: " + this.mShowDialog
            + "\n\t  isStartNextTest" + this);

        if (className.equals("com.example.lxn.agingtestnew.testItem.AudioTestItem")) {
            AudioTestItem audioTestItem = new AudioTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) audioTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.BackLightTestItem")) {
//            Log.d(TAG, "555555555555555555555555");
            BackLightTestItem backLightTestItem = new BackLightTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) backLightTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.BatteryTestItem")) {
            BatteryTestItem batteryTestItem = new BatteryTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) batteryTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.BluetoothTestItem")) {
            BluetoothTestItem bluetoothTestItem = new BluetoothTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) bluetoothTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.CameraTestItem")) {
            CameraTestItem cameraTestItem = new CameraTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) cameraTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.EmmcTestItem")) {
            EmmcTestItem emmcTestItem = new EmmcTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) emmcTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.GpsTestItem")) {
            GpsTestItem gpsTestItem = new GpsTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) gpsTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.GraTestItem")) {
            GraTestItem graTestItem = new GraTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) graTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.LcdTestItem")) {
            LcdTestItem lcdTestItem = new LcdTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) lcdTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.LightTestItem")) {
            LightTestItem lightTestItem = new LightTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) lightTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.MemoryTestItem")) {
            MemoryTestItem memoryTestItem = new MemoryTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) memoryTestItem;
//        } else if (className.equals("com.example.lxn.agingtestnew.testItem.ProTestItem")) {
//            ProTestItem proTestItem = new ProTestItem(resLayoutId);
//            this.mTestItem = (AbstractBaseTestItem) proTestItem;
//        } else if (className.equals("com.example.lxn.agingtestnew.testItem.QcTestItem")) {
//            QcTestItem qcTestItem = new QcTestItem(resLayoutId);
//            this.mTestItem = (AbstractBaseTestItem) qcTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.RebootTestItem")) {
            RebootTestItem rebootTestItem = new RebootTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) rebootTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.SensorTestItem")) {
            SensorTestItem sensorTestItem = new SensorTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) sensorTestItem;
//        } else if (className.equals("com.example.lxn.agingtestnew.testItem.HeadphoneTestItem")) {
//            HeadphoneTestItem headphoneTestItem = new HeadphoneTestItem(resLayoutId);
//            this.mTestItem = (AbstractBaseTestItem) headphoneTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.VibratorTestItem")) {
            VibratorTestItem vibratorTestItem = new VibratorTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) vibratorTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.VideoTestItem")) {
            VideoTestItem videoTestItem = new VideoTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) videoTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.WifiTestItem")) {
            WifiTestItem wifiTestItem = new WifiTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) wifiTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.ChargingTestItem")) {
            ChargingTestItem chargingTestItem = new ChargingTestItem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) chargingTestItem;
        } else if (className.equals("com.example.lxn.agingtestnew.testItem.EarpieceTestitem")) {
            EarpieceTestitem earpieceTestitem = new EarpieceTestitem(resLayoutId);
            this.mTestItem = (AbstractBaseTestItem) earpieceTestitem;
        }
        Log.d(TAG,"loadTestData this.mTestItem==="+this.mTestItem);
//        try {
//
//        	Class clazz = Class.forName(className);
//        	Constructor constructor = clazz.getDeclaredConstructor(new Class[]{int.class});
//        	Object obj = constructor.newInstance(new Object[]{Integer.valueOf(resLayoutId)});
//
//           // Object obj = Class.forName(className).getDeclaredConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(resLayoutId)});
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
        } else if (this.mTestType != 2) {
            this.mTestItem.testAutoRun(this.mHandler);
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
