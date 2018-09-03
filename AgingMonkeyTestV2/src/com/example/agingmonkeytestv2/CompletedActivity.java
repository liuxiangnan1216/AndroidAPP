package com.example.agingmonkeytestv2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/


public class CompletedActivity extends Activity {
	private static final String TAG = "lxn-CompletedActivity";
    public static final String COMPLETE_MESSAGE = "COMPLETE_MESSAGE";
    private TextView mCompletedMessageTv;
    private Button mExitBt;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_completed);

        this.mExitBt = (Button) findViewById(R.id.exit);
        this.mCompletedMessageTv = (TextView) findViewById(R.id.message_completed);
        FactoryApplication.stopMtkLog();

        this.mExitBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CompletedActivity.this.finish();
                Intent intent = new Intent();
                intent.setClass(CompletedActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                CompletedActivity.this.startActivity(intent);
                Log.d(TAG, "exit Completed Activity");

            }
        });
        initMessage(getIntent());
        Log.d(TAG, "CompletedActivity started at " + new Date().toGMTString());
    }

    /**
     * 
     * @param intent
     */
    private void initMessage(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String message = bundle.getString(COMPLETE_MESSAGE, "");
            if (!message.equals("")) {
                this.mCompletedMessageTv.setText(message);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return 4 == keyCode || 82 == keyCode || super.onKeyDown(keyCode, event);

    }
}


