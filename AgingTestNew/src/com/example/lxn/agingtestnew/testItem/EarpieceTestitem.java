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
 *   > DATE: Date on 19-1-10.
 ************************************************************************/


public class EarpieceTestitem extends AbstractBaseTestItem {

    private static final String TAG = "lxn-EarpieceTestitem";

    private MediaPlayer mediaPlayer;// = new MediaPlayer();
    private AudioManager audioManager;
    private TextView tv_earpiece;
    private TextView tv_timer;
    private VideoView videoView;
    private long testDuration = 0;
    private int testing_num = 0;
    private long timer = 0;
    private String timeStr = "";
    private boolean isStopCount = false;


    public EarpieceTestitem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "is playing===" + !mediaPlayer.isPlaying()
                        + "is stop count====" + !isStopCount);

        if (!isStopCount) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                testing_num++;
                mHandler.sendEmptyMessage(0);
            }
            return false;
        } else {
            isTestEnd = true;
            onStop();
            onTestEnd();
            if (testDuration != 0) {
                mHandler.sendEmptyMessage(1);
            }
            return true;
        }

    }


    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void initView(View view) {
        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
        tv_earpiece = (TextView) view.findViewById(R.id.tv_earpiece);
        tv_timer = (TextView) view.findViewById(R.id.tv_timer);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.audio);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1f, 1f);
        testDuration = SettingsActivity.earpiece_time;

        countTimer();

    }

    public void setUp() {
        super.setUp();

    }


    public void tearDown() {
        super.tearDown();

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_earpiece.setText("正在播放...第 " + testing_num + " 次");
                    break;
                case 1:
                    tv_earpiece.setText("播放结束。");
                    CompletedActivity.earpieceStatus = "PASS";
                    break;
            }

        }
    };



    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isStopCount) {
                timer += 1000;
                timeStr = getFormatHMS(timer);
                tv_timer.setText(timeStr);
                if (timer >= testDuration) {
                    isStopCount = true;
                }
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
