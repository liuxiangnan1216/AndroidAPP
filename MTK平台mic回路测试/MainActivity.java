package com.example.lxn.sagerealmicloop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "lxn-MainActivity";
    private Button mBtnEar;
    private Button mBtnMic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnEar = (Button) findViewById(R.id.btnear);
        mBtnMic = (Button) findViewById(R.id.btnmic);

        mBtnEar.setOnClickListener(this.mEarOnClickListener);
        mBtnMic.setOnClickListener(this.mMicOnClickListener);

    }


    private View.OnClickListener mEarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, EarActivity.class);
            startActivity(intent);

        }
    };

    private View.OnClickListener mMicOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AudioLoopback.class);
            startActivity(intent);
        }
    };
}
