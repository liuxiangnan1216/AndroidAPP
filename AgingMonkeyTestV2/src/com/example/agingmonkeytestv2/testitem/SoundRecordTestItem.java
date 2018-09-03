package com.example.agingmonkeytestv2.testitem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Switch;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class SoundRecordTestItem extends AbstractBaseTestItem{

	
    private static final String TAG = SoundRecordTestItem.class.getSimpleName();
    private static String strTempFile = (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_");
    private long TEST_DURATION = 15000;
    private long WAIT_TIME = 2000;
    protected boolean isStopRecord;
    private MediaRecorder mMediaRecorder;
    boolean mNotStop = true;
    private File mRecAudioDir;
    private File mRecAudioFile;
    String mRecordOff;
    String mRecordOn;
    Switch mRecordSwitch;
    private ArrayList<String> recordFiles;
    private boolean sdCardExit;
    
    
	public SoundRecordTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
	protected void initView(View view) {
		Log.d(TAG, "initView");
		this.mRecordSwitch = (Switch) view.findViewById(R.id.switch_sound_record);
		this.mRecordOn = this.mContext.getString(R.string.text_soundrecord_on);
		this.mRecordOff = this.mContext.getString(R.string.text_soundrecord_off);
	}
	
	public boolean execTest(Handler handler) {
		Log.d(TAG, "execTest: ");
        SystemClock.sleep(this.WAIT_TIME);
        startRecord(handler);
        SystemClock.sleep(this.TEST_DURATION);
        stopRecord(handler);
        playRecord();
        SystemClock.sleep(this.TEST_DURATION);
        return true;
	}
	
	public void onTestStart() {
		super.onTestStart();
        this.mRecordSwitch.setChecked(true);
        this.mRecordSwitch.setText(R.string.text_soundrecord_on);
	}
	
    public void onTestCompleted() {
        super.onTestCompleted();
        this.mRecordSwitch.setChecked(false);
        this.mRecordSwitch.setText(R.string.text_soundrecord_off);
    }
    
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        this.mNotStop = false;
    }
    
    private void init() {
        Log.d(TAG, "init: ");
        this.sdCardExit = Environment.getExternalStorageState().equals("mounted");
        if (this.sdCardExit) {
            String path = "/storage/emulated/0/0SoundRecord";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            this.mRecAudioDir = new File(path);
            getRecordFiles();
            return;
        }
        Log.d(TAG, "storage path is not existed");
    }
    
    private void startRecord(Handler handler) {
        try {
            handler.sendEmptyMessage(BaseTestActivity.MSG_START);
            this.mRecAudioFile = File.createTempFile(strTempFile, ".amr", this.mRecAudioDir);
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mMediaRecorder.setOutputFormat(MediaRecorder.AudioSource.DEFAULT);
            this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioSource.DEFAULT);
            this.mMediaRecorder.setOutputFile(this.mRecAudioFile.getAbsolutePath());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            Log.d(TAG, "start sound record, mRecAudioFile : " + this.mRecAudioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private void stopRecord(Handler handler) {
        Log.d(TAG, "Stop Sound Record");
        handler.sendEmptyMessage(BaseTestActivity.MSG_COMPLETED);
        if (this.mRecAudioFile != null) {
            this.mMediaRecorder.stop();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
            this.isStopRecord = true;
        }
    }

    private void playRecord() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(String.valueOf(this.mRecAudioFile));
            Log.d(TAG, "playRecord file is " + String.valueOf(this.mRecAudioFile));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRecordFiles() {
        this.recordFiles = new ArrayList();
        if (this.sdCardExit) {
            File[] files = this.mRecAudioDir.listFiles();
            if (files != null) {
                int i = 0;
                while (i < files.length) {
                    if (files[i].getName().indexOf(".") >= 0 && files[i].getName().substring(files[i].getName().indexOf(".")).toLowerCase().equals(".amr")) {
                        this.recordFiles.add(files[i].getName());
                    }
                    i++;
                }
            }
        }
    }
	

}
