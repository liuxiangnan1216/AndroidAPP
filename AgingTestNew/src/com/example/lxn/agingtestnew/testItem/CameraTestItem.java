package com.example.lxn.agingtestnew.testItem;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.BaseTestActivity;
import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;
import com.example.lxn.agingtestnew.fragment.CameraFragment;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class CameraTestItem extends AbstractBaseTestItem {
    private static final String TAG = "lxn-CameraTestItem";

    private TextView mTV_times;
    private SoundPool mSoundPool;
    private int mAudioClickId = -1;
    private CameraFragment mCameraMainFragment;
    private CameraFragment mCameraSubFragment;
    private boolean mTestCameraMain = true;
    private int timesCount = 1;
    private int test_times = 0;



    public CameraTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        this.timesCount++;
        Log.d(TAG, "--execTest--" + timesCount);
        if (this.timesCount <= test_times * 2) {
            handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
            SystemClock.sleep(3000);
            for (int i = 0; i < 5 && !this.mStopTest; i++) {
                if (this.mTestCameraMain) {
                    this.mCameraMainFragment.takePicture();
//                    this.mCameraMainFragment.saveJpeg();
                    this.mSoundPool.play(this.mAudioClickId, 1.0f, 1.0f, 0, 0, 1.0f);
                } else {
                    this.mCameraSubFragment.takePicture();
                    this.mSoundPool.play(this.mAudioClickId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
                SystemClock.sleep(2000);
            }
            if (!this.mStopTest) {
                SystemClock.sleep(400);
            }
            return false;
        } else {
//            onStop();
            if (test_times != 0) {
                CompletedActivity.cameraStatus = "PASS";
            }
            onTestEnd();
            return true;
        }
    }

    public void onStop() {
        super.onStop();
        mCameraMainFragment.releaseCamera();
        mCameraSubFragment.releaseCamera();
        this.mTV_times.setText("Test Completed");
        Log.d(TAG, "--onStop--");
    }

    public void setUp() {
        Log.d(TAG, "--setUp--");
        this.mTV_times.setText("Test Times:" + (this.timesCount / 2));
        doCameraSwitch();
    }

    public void onTestCompleted() {
        super.onTestCompleted();
        Log.d(TAG, "--onTestCompleted--");
        if (this.mSoundPool != null) {
            this.mSoundPool.release();
            this.mSoundPool = null;
        }
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "--initView--");
        mTV_times = (TextView) view.findViewById(R.id.tv_times);
        test_times = SettingsActivity.camera_times;

        SoundPool.Builder builder = new SoundPool.Builder();
        AudioAttributes.Builder builder1 = new AudioAttributes.Builder();
        builder1.setLegacyStreamType(AudioManager.STREAM_MUSIC);
        builder.setMaxStreams(1);
        builder.setAudioAttributes(builder1.build());
        this.mSoundPool = builder.build();
        this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d(CameraTestItem.this.TAG, "Load Completed " + sampleId);
            }
        });

        this.mAudioClickId = this.mSoundPool.load(view.getContext(), R.raw.camera_click, 1);
        CameraManager cameraManager = (CameraManager) this.mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] ids = cameraManager.getCameraIdList();
            for (String id : ids) {
                Log.d(TAG, "initView" + id);
            }
            for (String cameraId :ids) {
                int facing = ((Integer) cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING)).intValue();
                Log.d(TAG, "initView facing:" + facing);
                if (facing == 1){
                    this.mCameraMainFragment = CameraFragment.newInstance(Integer.parseInt(cameraId));
                } else {
                    this.mCameraSubFragment = CameraFragment.newInstance(Integer.parseInt(cameraId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doCameraSwitch() {
        Log.d(TAG, "--doCameraSwitch--");
        this.mTestCameraMain = !this.mTestCameraMain;
        if (this.mTestCameraMain && this.mCameraMainFragment != null) {
            this.mActivity.getFragmentManager().beginTransaction().replace(R.id.frame_camera, this.mCameraMainFragment).commit();
        } else if (this.mCameraSubFragment != null) {
            this.mActivity.getFragmentManager().beginTransaction().replace(R.id.frame_camera, this.mCameraSubFragment).commit();
        } else {
            throw new RuntimeException("some exception happened in camera preview test");
        }
    }
}
