package com.example.agingmonkeytestv2.testitem;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.FactoryApplication;
import com.example.agingmonkeytestv2.R;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class AudioTestItem extends AbstractBaseTestItem {
    private static final List<String> mMicrophoneAudioResourceNames = Arrays.asList(new String[]{"0Hz_-20dB_30s_15.5kHz.mp3", "1.5khz_0dB_30s_13.5kHz.mp3", "100Hz_0dB_30s_5kHz.mp3", "1khz_0dB_30s_15.5kHz.mp3", "200Hz_0dB_30s_6kHz.mp3", "22khz_-45dB_30s_22kHz.wav", "2khz_0dB_30s_13kHz.mp3", "300Hz_0dB_30s_6kHz.mp3", "3khz_0dB_30s_15.5kHz.mp3", "400Hz_0dB_30s_9kHz.mp3", "4khz_0dB_30s_16.5kHz.mp3", "500Hz_0dB_30s_8kHz.mp3", "5khz_0dB_30s_22kHz.mp3", "600Hz_0dB_30s_11.5kHz.mp3", "6khz_0dB_30s_15.5kHz.mp3", "700Hz_0dB_30s_11.5kHz.mp3", "7khz_0dB_30s_24kHz.mp3", "800Hz_0dB_30s_12kHz.mp3", "8khz_0dB_30s_15.5kHz.mp3", "900Hz_0dB_30s_12kHz.mp3", "Mute_30s.mp3"});
    private static final List<String> mSpeakerAudioResourceNames = Arrays.asList(new String[]{"0Hz_-20dB_30s_15.5kHz.mp3", "1.5khz_0dB_30s_13.5kHz.mp3", "100Hz_0dB_30s_5kHz.mp3", "10khz_0dB_30s_22.5kHz.mp3", "11khz_0dB_30s_16kHz.mp3", "12khz_0dB_30s_15.5kHz.mp3", "13khz_0dB_30s_22kHz.mp3", "14kHz_0dB_30s_16kHz.mp3", "15kHz_0dB_30s_17kHz.mp3", "16KHz_0dB_30s_16.4kHz.wav", "17KHz_0dB_30s_17.3kHz.wav", "18KHz_0dB_30s_18.2kHz.wav", "19KHz_0dB_30s_19.8kHz.wav", "1khz_0dB_30s_15.5kHz.mp3", "200Hz_0dB_30s_6kHz.mp3", "20KHz_0dB_30s_21.5kHz.wav", "22khz_-45dB_30s_22kHz.wav", "2khz_0dB_30s_13kHz.mp3", "300Hz_0dB_30s_6kHz.mp3", "3khz_0dB_30s_15.5kHz.mp3", "400Hz_0dB_30s_9kHz.mp3", "48khz_-20dB_1s_48kHz.wav", "4khz_0dB_30s_16.5kHz.mp3", "500Hz_0dB_30s_8kHz.mp3", "5khz_0dB_30s_22kHz.mp3", "600Hz_0dB_30s_11.5kHz.mp3", "6khz_0dB_30s_15.5kHz.mp3", "700Hz_0dB_30s_11.5kHz.mp3", "7khz_0dB_30s_24kHz.mp3", "800Hz_0dB_30s_12kHz.mp3", "8khz_0dB_30s_15.5kHz.mp3", "900Hz_0dB_30s_12kHz.mp3", "9khz_0dB_30s_15.5kHz.mp3", "Mute_30s.mp3"});
    private long TEST_DURATION = 3000;
    private final Object lock = new Object();
    private AudioManager mAudioManager;
    private TextView mAudioName;
    private ArrayList<String> mAudioResourceName = new ArrayList();
    private Context mContext;
    private Handler mHandler;
    private boolean mOutputMicrophone = true;
    private CheckBox mOutputMicrophoneCheckBox;
    private CheckBox mOutputSpeakerCheckBox;
    private boolean mPlayCompleted = false;
    private int mPlayIndex = -1;
    private MediaPlayer mPlayer;
    private boolean mSpeakerPhoneOn = false;
    private boolean mStop = false;
    private int mTruelyIndex = -1;

    public AudioTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    public boolean execTest(Handler handler) {
        this.mHandler = handler;
        this.mHandler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
        if (!this.mStop) {
            playAudioWithMicrophone(this.mContext, getNextIndex(true));
            waitForPlayCompleted();
        }
        if (!this.mStop) {
        	this.mHandler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
            playAudioWithSpeaker(this.mContext, getNextIndex(false));
            waitForPlayCompleted();
        }
        if (!this.mStop) {
            SystemClock.sleep(this.TEST_DURATION);
            this.mHandler.sendEmptyMessage(BaseTestActivity.MSG_TEAR_DOWN);
        }
        return true;
    }

    protected void initView(View view) {
        this.mAudioName = (TextView) view.findViewById(R.id.audio_name);
        this.mOutputMicrophoneCheckBox = (CheckBox) view.findViewById(R.id.output_microphone);
        this.mOutputSpeakerCheckBox = (CheckBox) view.findViewById(R.id.output_speaker);
        this.mContext = view.getContext();
        initResource();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
        this.mSpeakerPhoneOn = this.mAudioManager.isSpeakerphoneOn();
    }

    public void setUp() {
        super.setUp();
        this.mOutputMicrophoneCheckBox.setChecked(this.mOutputMicrophone);
        this.mOutputSpeakerCheckBox.setChecked(!this.mOutputMicrophone);
        this.mAudioName.setText("Playing: " + ((String) mSpeakerAudioResourceNames.get(this.mPlayIndex)));
    }

    public void tearDown() {
        super.tearDown();
        synchronized (this.lock) {
            if (this.mPlayer != null) {
                if (this.mPlayer.isPlaying()) {
                    this.mPlayer.pause();
                }
                this.mPlayer.release();
                this.mPlayer = null;
            }
        }
        if (this.mSpeakerPhoneOn) {
            this.mAudioManager.setSpeakerphoneOn(true);
        } else {
            this.mAudioManager.setSpeakerphoneOn(false);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mStop = true;
    }

    private void initResource() {
        int i;
        this.mAudioResourceName.clear();
        for (i = 97; i <= 122; i++) {
            this.mAudioResourceName.add(((char) i) + "");
        }
        for (i = 97; i <= 104; i++) {
            this.mAudioResourceName.add(FactoryApplication.AUDIO_RESOURCE_NAME + ((char) i));
        }
        Log.d(this.TAG, "initResource: mAudioRescource size" + this.mAudioResourceName.size());
        if (this.mAudioResourceName.size() != mSpeakerAudioResourceNames.size()) {
            throw new RuntimeException("audio resource not match with the given");
        }
    }

    private int getNextIndex(boolean microphone) {
        this.mTruelyIndex = new Random().nextInt(this.mAudioResourceName.size());
        if (this.mTruelyIndex == this.mAudioResourceName.size()) {
            this.mTruelyIndex = 0;
        }
        if (microphone) {
            this.mPlayIndex = mSpeakerAudioResourceNames.indexOf((String) mMicrophoneAudioResourceNames.get(this.mTruelyIndex % mMicrophoneAudioResourceNames.size()));
        } else {
            this.mPlayIndex = this.mTruelyIndex;
        }
        return this.mPlayIndex;
    }

    private void waitForPlayCompleted() {
        int i = 0;
        while (!this.mPlayCompleted && !this.mStop) {
            SystemClock.sleep(2000);
            i++;
            Log.d(this.TAG, "waitForPlayCompleted total " + i);
        }
        Log.d(this.TAG, "waitForPlayCompleted, mPlayCompleted:" + this.mPlayCompleted + ", mStop:" + this.mStop);
    }

    private void playAudioWithMicrophone(Context context, int index) {
        this.mOutputMicrophone = true;
        playAudio(context, index);
    }

    private void playAudioWithSpeaker(Context context, int index) {
        this.mOutputMicrophone = false;
        playAudio(context, index);
    }

    private void createMediaPlayer(Context context, int index) {
        synchronized (this.lock) {
            if (this.mPlayer != null) {
                this.mPlayer.release();
                this.mPlayer = null;
            }
        }
        int resId = context.getResources().getIdentifier((String) this.mAudioResourceName.get(index), "raw", context.getPackageName());
        Log.d(this.TAG, "playAudio: " + ((String) this.mAudioResourceName.get(index)));

        //Builder builder = new Builder();
        //AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        if (this.mOutputMicrophone) {
            mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);//builder.setLegacyStreamType(0);
        } else {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//builder.setLegacyStreamType(3);
        }
        this.mPlayer = MediaPlayer.create(context, resId);
    }

    private void playAudio(Context context, int index) {
        createMediaPlayer(context, index);
        switchOutput(this.mOutputMicrophone);
        this.mPlayCompleted = false;
        this.mPlayer.start();
        this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                AudioTestItem.this.mPlayCompleted = true;
                Log.d(AudioTestItem.this.TAG, "onCompletion play index  " + AudioTestItem.this.mPlayIndex);
            }
        });
    }

    private void switchOutput(boolean microphone) {
        if (microphone) {
            this.mAudioManager.setSpeakerphoneOn(false);
            this.mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            ((Activity) this.mContext).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            return;
        }
        this.mAudioManager.setSpeakerphoneOn(true);
        this.mAudioManager.setMode(AudioManager.MODE_NORMAL);
        ((Activity) this.mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
}

