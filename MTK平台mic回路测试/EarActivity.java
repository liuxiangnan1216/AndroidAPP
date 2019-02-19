package com.example.lxn.sagerealmicloop;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-2-19.
 ************************************************************************/



public class EarActivity extends AppCompatActivity {

    private static final String TAG = "lxn-EarActivity";

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private Button mBtnStart;
    private Button mBtnStop;

    private Handler mHandler = new Handler();

    private TextView mTextView;
    private long timer = 0;
    private String timeStr = "";
    private boolean isStopCount = false;
//    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear);

        mBtnStart = (Button) findViewById(R.id.btnstart);
        mBtnStop = (Button) findViewById(R.id.btnstop);
        mTextView = (TextView) findViewById(R.id.tv_ear);

        initView();

        mBtnStart.setOnClickListener(this.mStartOnClickListener);
        mBtnStop.setOnClickListener(this.mStopOnClickListener);
        play();

    }


    protected void initView() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
//        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        int maxVolue = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolue, 0);
        mediaPlayer = MediaPlayer.create(this, R.raw.pink_0db);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1f, 1f);

        countTimer();

    }

    private void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
            });
        }

    }


    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "isPlaying:" + !mediaPlayer.isPlaying());
            play();
            isStopCount = false;

        }
    };

    private View.OnClickListener mStopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mediaPlayer.isPlaying()) {
                Log.d(TAG, "stop");
                mediaPlayer.stop();
                isStopCount = true;
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    protected void onDestory() {
        super.onDestroy();

        mediaPlayer.release();
        mediaPlayer = null;
        mHandler.removeCallbacks(TimerRunnable);
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
        mHandler.removeCallbacks(TimerRunnable);
    }

    protected void onResume() {
        super.onResume();
        play();
    }



    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isStopCount) {
                timer += 1000;
                timeStr = getFormatHMS(timer);
                mTextView.setText(timeStr);
            }
            countTimer();
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
    }

    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) (time / 60);//分
        int h = (int) (time / 3600);//时
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}

