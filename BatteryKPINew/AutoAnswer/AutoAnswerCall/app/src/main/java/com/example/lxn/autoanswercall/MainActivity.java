package com.example.lxn.autoanswercall;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lxn--antoAnsertMainActivity";
    public static String UTDPhoneNum = "15258370426";
    public static int call_count = 5;
    public static float current_call_count = 0.0f;
    private Context context;
    private static final String configFILE_NAME = "/myconfig.xml";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText textViewNumber;
    private EditText textViewCount;

    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        textViewNumber = (EditText)findViewById(R.id.et_incallnum);
        textViewCount = (EditText)findViewById(R.id.et_countnum);
        btn = (Button) findViewById(R.id.button);


        UTDPhoneNum = textViewNumber.getText().toString();
        call_count = Integer.parseInt(textViewCount.getText().toString());

        Settings.System.putString(context.getContentResolver(),"UTDPhoneNum",  UTDPhoneNum);
        Settings.System.putInt(context.getContentResolver(),"call_count",  call_count);
        Settings.System.putFloat(context.getContentResolver(),"current_call_count",  current_call_count);
        Log.d(TAG, "onCreate: UTDPhoneNum" + UTDPhoneNum);
        Log.d(TAG, "onCreate: call_count" + call_count);
        Log.d(TAG, "onCreate: current_call_count" + current_call_count);

        btn.setOnClickListener(this.mBtnOnClickListener);

    }


    public View.OnClickListener mBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "click_sp: ");
            if (!(textViewCount.getText().equals("") && textViewNumber.getText().equals(""))) {
                UTDPhoneNum = textViewNumber.getText().toString();
                call_count = Integer.parseInt(textViewCount.getText().toString());
            }

//                getcongif();
            Settings.System.putString(context.getContentResolver(), "UTDPhoneNum", UTDPhoneNum);
            Settings.System.putFloat(context.getContentResolver(), "call_count", call_count);
            Settings.System.putFloat(context.getContentResolver(), "current_call_count", current_call_count);
            Log.d(TAG, "click_sp: UTDPhoneNum" + UTDPhoneNum);
            Log.d(TAG, "click_sp: call_count" + call_count);
            Log.d(TAG, "click_sp: current_call_count" + current_call_count);
        }
    };

//    public void click_sp(View view) {
//        switch (view.getId()) {
//            case R.id.button:
//                Log.d(TAG, "click_sp: ");
//                if (!(textViewCount.getText().equals("") && textViewNumber.getText().equals(""))) {
//                    UTDPhoneNum = textViewNumber.getText().toString();
//                    call_count = Integer.parseInt(textViewCount.getText().toString());
//                }
//
////                getcongif();
//                Settings.System.putString(context.getContentResolver(), "UTDPhoneNum", UTDPhoneNum);
//                Settings.System.putFloat(context.getContentResolver(), "call_count", call_count);
//                Settings.System.putFloat(context.getContentResolver(), "current_call_count", current_call_count);
//                Log.d(TAG, "click_sp: UTDPhoneNum" + UTDPhoneNum);
//                Log.d(TAG, "click_sp: call_count" + call_count);
//                Log.d(TAG, "click_sp: current_call_count" + current_call_count);
//                break;
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

    public void myconnect(View v) {
        Log.i(TAG, "connectaaa: ");
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(context.TELECOM_SERVICE);
        Method method = null;
        try {
            method = Class.forName("android.telecom.TelecomManager").getMethod("acceptRingingCall");
            method.invoke(telecomManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



//
//    //获取myconfig.xml文件
//    public static void getcongif() {
//        Log.d(TAG, "getcongif: ");
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            // 获取SD卡对应的存储目录
//            File sdCardDir = Environment.getExternalStorageDirectory();
//            System.out.println("----------------" + sdCardDir);
//            // 获取指定文件对应的输入流
//            try {
//                FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath() + configFILE_NAME);
//                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//                parseXMLWithPull(br);
//            } catch (IOException e) {
//                Log.d(TAG, "getcongif: IOException e");
//                e.printStackTrace();
//            }
//        }
//    }

//    //解析xml文件
//    public static void parseXMLWithPull(BufferedReader bufferedReader) {
//        Log.d(TAG, "parseXMLWithPull: ");
//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser xmlPullParser = factory.newPullParser();
//            xmlPullParser.setInput(bufferedReader /*new StringReader(xmlData)*/);
//            int eventType = xmlPullParser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String nodeName = xmlPullParser.getName();
//                switch (eventType) {
//                    // 开始解析某个结点
//                    case XmlPullParser.START_TAG: {
//                        if ("UTDPhoneNum".equals(nodeName)) {
//                            UTDPhoneNum = xmlPullParser.nextText();
//                        } else if ("call_count".equals(nodeName)) {
//                            call_count = Float.parseFloat(xmlPullParser.nextText());
//                        }
//                        break;
//                    }
//                    // 完成解析某个结点
//                    case XmlPullParser.END_TAG: {
//                        if ("resources".equals(nodeName)) {
//                            Log.d("parseXMLWithPull",
//                                    "UTDPhoneNum" + UTDPhoneNum + "\n" +
//                                            "call_count" + call_count + "\n"
//
//                            );
//                        }
//                        break;
//                    }
//                    default:
//                        break;
//                }
//                eventType = xmlPullParser.next();
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "parseXMLWithPull: Exception e");
//            e.printStackTrace();
//        }
//    }
}
