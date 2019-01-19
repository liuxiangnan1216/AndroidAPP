package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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


public class VideoTestItem extends AbstractBaseTestItem {

    private static final String TAG = "lxn-VideoTestItem";

    private TextView tv_video;
    private VideoView videoView;
    private AudioManager audioManager;

    private int testTimes = 0;
    private int testing_num = 0;


    public VideoTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {

        Log.d(TAG, "execTest is Playing===" + videoView.isPlaying());

        if (!videoView.isPlaying()) {
            if (testing_num < testTimes) {
                videoView.start();
                Log.d(TAG, "test num ==" + testing_num);
                testing_num++;
                mHandler.sendEmptyMessage(0);
                SystemClock.sleep(500);
            } else {
//                onStop();
                mHandler.sendEmptyMessage(1);
                onTestEnd();
                return true;
            }
        }
        return false;
    }

    public void onStop() {
        super.onStop();
        mHandler.sendEmptyMessage(1);
//        videoView.suspend();
        videoView = null;

    }

    @Override
    protected void initView(View view) {
        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
        tv_video = (TextView) view.findViewById(R.id.tv_video);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://"
                + view.getContext().getPackageName() + "/raw/videotest"));
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "initview maxVolume==" + maxVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        testTimes = SettingsActivity.video_times;
        Log.d(TAG, "initView test times====" + testTimes);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_video.setText("正在播放...第 " + testing_num + " 次");
                    break;
                case 1:
                    tv_video.setText("播放结束。");
                    if (testTimes != 0) {
                        CompletedActivity.videoStatus = "PASS";
                    }
                    break;
            }

        }
    };

}
