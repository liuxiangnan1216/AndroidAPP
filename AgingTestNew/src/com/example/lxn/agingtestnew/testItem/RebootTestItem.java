package com.example.lxn.agingtestnew.testItem;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class RebootTestItem extends AbstractBaseTestItem {

    private static final String TAG = "lxn-RebootTestItem";

//    private EditText mEditText;
    private Button mRebootButton;
    private Context mContext;
    private TextView mTextView;
    private Button   mStopButton;

    public RebootTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        onStop();
        return true;
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        mContext = view.getContext();
        mRebootButton = (Button) view.findViewById(R.id.RebootButton);
        mStopButton = (Button) view.findViewById(R.id.StopButton);

//        mEditText = (EditText) view.findViewById(R.id.NumReboot);
        mTextView = (TextView) view.findViewById(R.id.RebootAccount);

        int RebootSetNum = Settings.Global.getInt(mContext.getContentResolver(), "rebootnum", 0);
        int RebootNowNum = Settings.Global.getInt(mContext.getContentResolver(), "rebootaccount", 0);
        int rebootFlag = Settings.Global.getInt(mContext.getContentResolver(), "rebootflag",0);


//        mEditText.setText(String.valueOf(RebootSetNum));
//        mEditText.setSelection((String.valueOf(RebootSetNum)).length());
        if(RebootNowNum > 0){
            mTextView.setText(String.valueOf(RebootNowNum - 1));
        } else {
            mTextView.setText(String.valueOf(RebootNowNum));
        }

        if (rebootFlag == 1) {
            mStopButton.setEnabled(true);
            mRebootButton.setEnabled(false);
//            mEditText.setEnabled(false);
        } else {
            mStopButton.setEnabled(false);
            mRebootButton.setEnabled(true);
//            mEditText.setEnabled(true);
        }

        RebootSetClickListener();
        StopSetClickListener();
        Log.d(TAG, "reboot times ====" + SettingsActivity.reboot_times);

    }


    private void RebootSetClickListener(){
        mRebootButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//                int NumReboot = SettingsActivity.reboot_times;//mEditText.getText().toString().trim();
//                if(TextUtils.isEmpty(mEditText.getText())){
//                    Toast.makeText(mContext, "请输入有效数字！", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                int Num = SettingsActivity.reboot_times;;//Integer.parseInt(NumString);
                if(Num <= 0 ){
                    Toast.makeText(mContext, "请输入有效数字！", Toast.LENGTH_SHORT).show();
                }else {

                    Settings.Global.putInt(mContext.getContentResolver(), "rebootnum", Num);
                    Settings.Global.putInt(mContext.getContentResolver(), "rebootaccount",Num);
                    Settings.Global.putInt(mContext.getContentResolver(), "rebootflag",1);
                    mStopButton.setEnabled(true);
                    mRebootButton.setEnabled(false);
//                    mEditText.setEnabled(false);
                    Intent reboot = new Intent("android.intent.action.REBOOT");
                    reboot.putExtra("android.intent.extra.KEY_CONFIRM", false);
                    reboot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(reboot);

                }
            }
        });
    }


    private void  StopSetClickListener(){
        mStopButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Settings.Global.putInt(mContext.getContentResolver(), "rebootflag",0);
                mStopButton.setEnabled(false);
                mRebootButton.setEnabled(true);
//                mEditText.setEnabled(true);
                Settings.Global.putInt(mContext.getContentResolver(), "rebootaccount",0);
                Settings.Global.putInt(mContext.getContentResolver(), "rebootnum", 0);
//                mEditText.setText("0");
                mTextView.setText("0");
            }
        });
    }
}
