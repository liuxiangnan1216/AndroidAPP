package com.example.agingmonkeytestv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class RebootTestActivity extends Activity{

	 private EditText mEditText;
	 private Button   mRebootButton;
	 private Context mContext;
	 private TextView mTextView;
	 private Button   mStopButton;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reboot);
        mRebootButton = (Button)findViewById(R.id.RebootButton);
        mStopButton = (Button)findViewById(R.id.StopButton);
        
        mEditText = (EditText)findViewById(R.id.NumReboot); 
        mTextView = (TextView)findViewById(R.id.RebootAccount);
        
        int RebootSetNum = Settings.Global.getInt(getContentResolver(), "rebootnum", 0);
        int RebootNowNum = Settings.Global.getInt(getContentResolver(), "rebootaccount", 0);
        int rebootFlag = Settings.Global.getInt(getContentResolver(), "rebootflag",0); 
        
       
        mEditText.setText(String.valueOf(RebootSetNum));
        mEditText.setSelection((String.valueOf(RebootSetNum)).length());
        if(RebootNowNum > 0){
        	mTextView.setText(String.valueOf(RebootNowNum - 1));
        } else {
        	mTextView.setText(String.valueOf(RebootNowNum));
        }
        
        if (rebootFlag == 1) {
	        mStopButton.setEnabled(true);
			mRebootButton.setEnabled(false);
			mEditText.setEnabled(false);
        } else {
        	mStopButton.setEnabled(false);
			mRebootButton.setEnabled(true);
			mEditText.setEnabled(true);
        }
        
        RebootSetClickListener();
        StopSetClickListener();
    }
    
    private void RebootSetClickListener(){
    	mRebootButton.setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RebootTestActivity.this.mContext = RebootTestActivity.this.getApplicationContext();
				String NumString=mEditText.getText().toString().trim();
				if(TextUtils.isEmpty(mEditText.getText())){
					Toast.makeText(RebootTestActivity.this.mContext, R.string.reboot_warning, Toast.LENGTH_SHORT).show();
					return;
				}
				
				int Num=Integer.parseInt(NumString);
				if(Num <= 0 ){
					Toast.makeText(RebootTestActivity.this.mContext, R.string.reboot_warning, Toast.LENGTH_SHORT).show();	
				}else {
				      
				    Settings.Global.putInt(getContentResolver(), "rebootnum", Num);
				    Settings.Global.putInt(getContentResolver(), "rebootaccount",Num);
				    Settings.Global.putInt(getContentResolver(), "rebootflag",1); 
				    mStopButton.setEnabled(true);
				    mRebootButton.setEnabled(false);
				    mEditText.setEnabled(false);
	                Intent reboot = new Intent("android.intent.action.REBOOT");
	                reboot.putExtra("android.intent.extra.KEY_CONFIRM", false);
	                reboot.setFlags(268435456);
	                RebootTestActivity.this.mContext.startActivity(reboot);
	                
				}
    		}	
    	}); 
    }
    
  
    private void  StopSetClickListener(){
    	mStopButton.setOnClickListener(new View.OnClickListener(){
    
            @Override
			public void onClick(View arg0) {
				Settings.Global.putInt(getContentResolver(), "rebootflag",0); 
				mStopButton.setEnabled(false);
				mRebootButton.setEnabled(true);
				mEditText.setEnabled(true);
				Settings.Global.putInt(getContentResolver(), "rebootaccount",0);
				Settings.Global.putInt(getContentResolver(), "rebootnum", 0);
				mEditText.setText("0");
				mTextView.setText("0");
			}
    	});
    }
}
