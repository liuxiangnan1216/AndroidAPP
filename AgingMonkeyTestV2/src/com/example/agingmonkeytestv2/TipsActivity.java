package com.example.agingmonkeytestv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-7.
 ************************************************************************/


public class TipsActivity extends Activity{
    private static final String TAG = "lxn-TipsActivity";
    private TextView textViewTips;
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_tips);
        textViewTips = (TextView) findViewById(R.id.tips);
        textViewTips.setText(readContent());
    }

    private String readContent() {
        StringBuffer stringBuffer = new StringBuffer();
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("tips.txt"), "UTF-8"));
            Log.d(TAG, "bufferedReader===" +bufferedReader);
            while (true) {
                String string = bufferedReader.readLine();
                if (string == null) {
                    break;
                }
                stringBuffer.append(string);
                stringBuffer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TipsActivity", "readContent: " + stringBuffer.toString());
        return stringBuffer.toString();
    }
}
