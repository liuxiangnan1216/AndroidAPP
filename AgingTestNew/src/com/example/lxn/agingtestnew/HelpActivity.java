package com.example.lxn.agingtestnew;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-7.
 ************************************************************************/

public class HelpActivity extends Activity{
    private static final String TAG = "lxn-HelpActivity";
    private TextView textViewHelp;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_help);
        textViewHelp = (TextView) findViewById(R.id.help);
        textViewHelp.setText(readContent());
    }

    private String readContent() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("help.txt"), "UTF-8"));
            Log.d(TAG, "bufferedReader==== " + bufferedReader);
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
        return stringBuffer.toString();
    }
}
