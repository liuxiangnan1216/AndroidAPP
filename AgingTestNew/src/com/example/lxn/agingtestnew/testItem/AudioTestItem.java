package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;



/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class AudioTestItem extends AbstractBaseTestItem {

    private static final String TAG = "lxn-AudioTestItem";

    private MediaPlayer mediaPlayer;// = new MediaPlayer();
    private AudioManager audioManager;
    private TextView tv_audio;
    private VideoView videoView;
    private int playTimes = 0;
    private int testCount = 1;
    private int testing_num = 1;


    public AudioTestItem(int resLayout) {
        super(resLayout);
    }


    @Override
    public boolean execTest(Handler handler) {

        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying() && testCount <= playTimes) {
                mediaPlayer.start();
                testCount++;
                mHandler.sendEmptyMessage(0);
            }
        }
        if (playTimes <= testCount && !mediaPlayer.isPlaying()) {
//            onStop();
            onTestEnd();
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (playTimes != 0) {
                mHandler.sendEmptyMessage(1);
            }
            return true;
        }
        return false;
    }


    public void onTestEnd() {
        super.onTestEnd();
    }

    protected void initView(View view) {
        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
        tv_audio = (TextView) view.findViewById(R.id.tv_audio);
        playTimes = SettingsActivity.audio_times;

        audioManager.setMode(AudioManager.MODE_NORMAL);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.audio);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1f, 1f);
        Log.d(TAG, "initView test times====" + playTimes);


    }

    public void onStop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setUp() {
        super.setUp();

    }


    public void tearDown() {
        super.tearDown();

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_audio.setText("正在播放...第 " + (testing_num++) + " 次");
                    break;
                case 1:
                    tv_audio.setText("播放结束。");
                    CompletedActivity.audioStatus = "PASS";
                    break;
            }

        }
    };


}
