package com.lxn.android.testing.uiautomator.calculator;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com
 *   > DATE: Date on 18-7-20.
 ************************************************************************/

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertThat;



@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class CalculatorTest {
    private static final String TAG = "lxn-test";

    private static final String FLAG = "com.android.calculator2:id/pad_numeric";//找一个主界面 flag

    private static final int LAUNCH_TIMEOUT = 2000;

    private UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    //private static final String LAUNCH_NAME = "com.android.launcher3";
    private static final String ALLAPPS = "all_app_handle";
    private static final String TEST_PACKAGE_NAME = "com.android.calculator2";
    private static final String STRING_TO_BE_TYPED = "";

    //定义ID
    private static final String PAD_OP = "pad_operator";
    private static final String PAD_NUM = "pad_numeric";
    private static final String PAD_ADV = "pad_advanced";

    private static final String[] numberId = {"digit_0", "digit_1", "digit_2", "digit_3", "digit_4",  "digit_5", "digit_6", "digit_7", "digit_8", "digit_9"};//数字id
    //private String[] OPid = {"op_add", "op_mul", "op_sub", "op_div"};
    private static final String ADD = "op_add";//+
    private static final String SUB = "op_sub";//-
    private static final String MUL = "op_mul";//*
    private static final String DIV = "op_div";// /
    private static final String POINT = "dec_point";// .
    private static final String EQ = "eq";// =
    private static final String DEL = "del";//DEL
    private static final String CLR = "clr";//CLR
    private static final String RESULT = "com.android.calculator2:id/result";//
    private static final String MODE = "RAD";//RAD
    private static final String DISPLAY = "display";//
    private static final String FORMULA = "com.android.calculator2:id/formula";

    private static final String INV = "toggle_inv";//inv
    private static final String PER = "op_pct";//%
    private static final String SIN = "fun_sin";//sin
    private static final String ARCSIN = "fun_arcsin";
    private static final String COS = "fun_cos";//cos
    private static final String ARCCOS = "fun_arccos";
    private static final String TAN = "fun_tan";//tan
    private static final String ARCTAN = "fun_arctan";
    private static final String PI = "const_pi";//PI
    private static final String LN = "fun_ln";//ln
    private static final String EXP = "fun_exp";//
    private static final String LOG = "fun_log";//log
    private static final String FUN_POW = "fun_10pow";//
    private static final String FACT = "op_fact";//!
    private static final String E = "const_e";//e
    private static final String LPAREN = "lparen";//(
    private static final String RPAREN = "rparen";//)
    private static final String SQRT = "op_sqrt";//squ
    private static final String SQR = "op_sqr";
    private static final String POW = "op_pow";//^
    private static final String DEG = "toggle_mode";//deg
    //private static final String RAD = "";

    private  String clrordelText = "DEL";
//    private boolean isDEG = false;

    private int tvLen = 15;

    private String displayTextView;
    private String resultTextView;
    private String formulaTextView;

    private UiObject display = mDevice.findObject(new UiSelector().resourceId(DISPLAY));
    private UiObject result = mDevice.findObject(new UiSelector().resourceId(RESULT));
    private UiObject formula = mDevice.findObject(new UiSelector().resourceId(FORMULA));



    @Before
    public void startMainActivityFromHomeScreen() {
        // wakeup screen
        try {
            if (!mDevice.isScreenOn()) {
                Log.e(TAG, "isScreenOn=="+mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }


        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);


        //如果测试应用的主 activity 未显示 则开启
        if (mDevice.wait(Until.hasObject(By.pkg(TEST_PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT)) {
            //如果应用已经启动，判断是否是在 main activity 不在重新启动  new UiObject
            Log.e(TAG, "应用已经启动");

            UiObject mainActivityFlag = mDevice.findObject(new UiSelector().resourceId(FLAG));
            UiObject clrordel = mDevice.findObject(new UiSelector().text("DEL"));

            Log.e(TAG, "clrordel.exists()====="+clrordel.exists());
            Log.e(TAG, "mainActivityFlag.exists()====="+mainActivityFlag.exists());

            if (!clrordel.exists() && !mainActivityFlag.exists()) {//根据 main activity 的元素是否存在判断是否重新启动
                Log.e(TAG, "重新启动app");

                startApp(TEST_PACKAGE_NAME, LAUNCH_TIMEOUT);
            }

        } else {
            Log.e(TAG, "应用未启动，开启应用");
            startApp(TEST_PACKAGE_NAME, LAUNCH_TIMEOUT);
        }

        //init calculator UI
        clickDelOrClr(true);
        mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
        if (mDevice.findObject(By.res(TEST_PACKAGE_NAME, ARCTAN)) != null) {
            clickNumberOrOp(INV);
        }
        mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
    }

/***************************************测试 CASE  begin**************************************************/
    /**
     * tool-001
     * 1、Idle，打开计算器
     * 2、输入1、2、3、4、5、6、7、8、9、.、0
     */
    @Test
    public void test_tool_001(){
        try {
            sleep(500);
            for (int i = 1; i < 10; i ++){
                clickNumberOrOp(numberId[i]);
            }
            clickNumberOrOp(POINT);
            clickNumberOrOp(numberId[0]);
            formulaTextView = formula.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  catch (UiObjectNotFoundException e) {
            Log.e(TAG, "UiObjectNotFoundException");
            e.printStackTrace();
        }
        clickDelOrClr(true);//mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEL)).longClick();
        assertEquals("test_tool_001", "123,456,789.0", formulaTextView);
    }

    /**
     * tool-002
     * 1、主菜单界面-打开计算器
     * 2、当计算器界面无内容时直接输入小数点“ . ”
     */
    @Test
    public void test_tool_002(){
        try {
            clickNumberOrOp(POINT);
            formulaTextView = formula.getText().trim();
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test_tool_002", ".", formulaTextView);
    }


    /**
     * tool-003
     * 1、主菜单界面-打开计算器
     * 2、当计算器输出框中无小数值（如：123）时输入小数点“ . ”
     */
    @Test
    public void test_tool_003(){
        try {
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(POINT);
            formulaTextView = formula.getText().trim();
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test_tool_001", "123.", formulaTextView);
    }
    /**
     * tool-004
     * 1、主菜单界面-打开计算器
     * 2、当计算器输出框中已有小数值（如：123.1）时再输入小数点“ . ”
     */
    @Test
    public void test_tool_004(){
        try {
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(POINT);
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(POINT);
            sleep(500);
            formulaTextView = formula.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test_tool_004", "123.1", formulaTextView);

    }

    /**
     * tool-005
     * Pre：测试机处于竖屏
     * 1、主菜单界面-打开计算器
     * 2、进入计算器后将手机横屏放置
     * 3、横屏显示后输入任一字符，点击任一按钮
     */
    @Test
    public void test_tool_005(){
        try {

            mDevice.setOrientationLeft();
            sleep(500);
            for (int i = 0; i < numberId.length; i ++){
                clickNumberOrOp(numberId[i]);
            }
            clickNumberOrOp(POINT);
            clickNumberOrOp(ADD);
            clickNumberOrOp(MUL);
            clickNumberOrOp(SUB);
            clickNumberOrOp(DIV);
            sleep(500);
            formulaTextView = formula.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        try {
            //sleep(500);
            mDevice.setOrientationNatural();//.setOrientationRight();
            //sleep(500);
            //mDevice.unfreezeRotation();
           // sleep(1000);
        } catch (RemoteException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        assertEquals("test_tool_005", "0,123,456,789.÷", formulaTextView);

    }
    /**
     * tool-006
     * 1、打开计算器
     * 2、进行加法运算
     */
    @Test
    public void test_tool_006(){
        try {

            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(ADD);
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
            Log.e(TAG, "test 006 ===="+ resultTextView);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test tool-006","246", resultTextView);

    }
    /**
     * tool-007
     * 1、打开计算器
     * 2、进行减法运算
     */
    @Test
    public void test_tool_007(){
        try {
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[4]);
            clickNumberOrOp(numberId[6]);
            clickNumberOrOp(SUB);
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
            Log.e(TAG, "test 007 ===="+ resultTextView);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test tool-007","123", resultTextView);
    }
    /**
     * tool-008
     * 1、打开计算器
     * 2、进行乘法运算
     */
    @Test
    public void test_tool_008(){
        try {

            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[4]);
            clickNumberOrOp(numberId[6]);
            clickNumberOrOp(MUL);
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
            Log.e(TAG, "test 008 ===="+ resultTextView);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test tool-008","30,258", resultTextView);
    }
    /**
     * tool-009
     * 1、打开计算器
     * 2、进行除法运算
     */
    @Test
    public void test_tool_009(){
        try {

            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[4]);
            clickNumberOrOp(numberId[6]);
            clickNumberOrOp(DIV);
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(numberId[3]);
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
            Log.e(TAG, "test 009 ===="+ resultTextView);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        assertEquals("test tool-009","2", resultTextView);
    }

    /**
     * tool-010
     * 1、打开计算器
     * 2、进行sin运算
     */
    @Test
    public void test_tool_010(){
        String degOrRad = "RAD";
        try {
            clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(SIN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            //sleep(500);
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(numberId[0]);
            //sleep(500);
            clickNumberOrOp(PAD_ADV);
            sleep(500);
            clickNumberOrOp(RPAREN);

            degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            //formulaTextView = formula.getText().trim();
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        if (degOrRad.equals("RAD")) {
            assertEquals("test tool-010", "0.5", resultTextView);
        } else {
            if (resultTextView.length() > 20) {
                assertEquals("test tool-010", "−0.988031624092861789987748", resultTextView);
            } else {
                assertEquals("test tool-010", "−0.988031624092", resultTextView);
            }
        }
    }

    /**
     * tool-011
     * 1、打开计算器
     * 2、进行cos运算
     */
    @Test
    public void test_tool_011(){
        String degOrRad = "RAD";
        try {
            clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(COS);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            //sleep(500);
            clickNumberOrOp(numberId[6]);
            clickNumberOrOp(numberId[0]);
            //sleep(500);
            clickNumberOrOp(PAD_ADV);
            sleep(500);
            clickNumberOrOp(RPAREN);
            degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            //formulaTextView = formula.getText().trim();
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        if (degOrRad.equals("RAD")) {
            assertEquals("test tool-011", "0.5", resultTextView);
        } else {
            if (resultTextView.length() > 20) {
                assertEquals("test tool-011", "−0.95241298041515629263816", resultTextView);
            } else {
                assertEquals("test tool-011", "−0.952412980415", resultTextView);
            }
        }

    }

    /**
     * tool-012
     * 1、打开计算器
     * 2、进行tan运算
     */
    @Test
    public void test_tool_012(){
        String degOrRad = "RAD";
        try {
            clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(TAN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            //sleep(500);
            clickNumberOrOp(numberId[4]);
            clickNumberOrOp(numberId[5]);
            //sleep(500);
            clickNumberOrOp(PAD_ADV);
            sleep(500);
            clickNumberOrOp(RPAREN);
            degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            //formulaTextView = formula.getText().trim();
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        if (degOrRad.equals("RAD")) {
            assertEquals("test tool-012", "1", resultTextView);
        } else {
            if (resultTextView.length() > 18) {
                assertEquals("test tool-012", "1.6197751905438615499827965", resultTextView);
            } else {
                assertEquals("test tool-012", "1.6197751905438", resultTextView);
            }
        }

    }

    /**
     * tool-013
     * 1、打开计算器
     * 2、进行ln运算
     */
    @Test
    public void test_tool_013(){
        try {
            clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(LN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            //sleep(500);
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(numberId[0]);
            //sleep(500);
            clickNumberOrOp(PAD_ADV);
            sleep(500);
            clickNumberOrOp(RPAREN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            //formulaTextView = formula.getText().trim();
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-013", "3.4011973816621553754132366", resultTextView);
        } else {
            assertEquals("test tool-013", "3.4011973816621", resultTextView);
        }
    }


    /**
     * tool-014
     * 1、打开计算器
     * 2、进行log运算
     */
    @Test
    public void test_tool_014(){
        try {
            clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(LOG);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            //sleep(500);
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(numberId[0]);
            //sleep(500);
            clickNumberOrOp(PAD_ADV);
            sleep(500);
            clickNumberOrOp(RPAREN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            //formulaTextView = formula.getText().trim();
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-014", "1.4771212547196624372950279", resultTextView);
        } else {
            assertEquals("test tool-014", "1.4771212547196", resultTextView);
        }
    }

    /**
     * tool-015
     * 1、打开计算器
     * 2、进行!运算
     */
    @Test
    public void test_tool_015(){
        try {

            clickNumberOrOp(numberId[5]);
            sleep(500);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            //clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(FACT);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        assertEquals("test tool-015","120", resultTextView);
    }

    /**
     * tool-016
     * 1、打开计算器
     * 2、PI
     */
    @Test
    public void test_tool_016(){
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            //clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(PI);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-016", "3.1415926535897932384626433", resultTextView);
        } else {
            assertEquals("test tool-016", "3.1415926535897", resultTextView);
        }
    }

    /**
     * tool-017
     * 1、打开计算器
     * 2、进行PI运算
     */
    @Test
    public void test_tool_017(){
        try {

            clickNumberOrOp(numberId[5]);
            sleep(500);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            //clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(PI);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-017", "15.707963267948966192313216", resultTextView);
        } else {
            assertEquals("test tool-017", "15.707963267948", resultTextView);
        }
    }

    /**
     * tool-018
     * 1、打开计算器
     * 2、e显示
     */
    @Test
    public void test_tool_018(){
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            //clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(E);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-018", "2.7182818284590452353602874", resultTextView);
        } else {
            assertEquals("test tool-018", "2.7182818284590", resultTextView);
        }
    }

    /**
     * tool-019
     * 1、打开计算器
     * 2、进行e运算
     */
    @Test
    public void test_tool_019(){
        try {
            clickNumberOrOp(numberId[5]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            //clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(E);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            sleep(500);
            //clickNumberOrOp(numberId[5]);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-019", "13.591409142295226176801437", resultTextView);
        } else {
            assertEquals("test tool-019", "13.591409142295", resultTextView);
        }

    }


    /**
     * tool-020
     * 1、打开计算器
     * 2、进行^运算
     */
    @Test
    public void test_tool_020(){
        try {
            clickNumberOrOp(numberId[5]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            clickNumberOrOp(POW);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            sleep(500);
            clickNumberOrOp(numberId[5]);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        assertEquals("test tool-020", "3,125", resultTextView);

    }

    /**
     * tool-021
     * 1、打开计算器
     * 2、进行 开根 运算
     */
    @Test
    public void test_tool_021(){
        try {
            //clickNumberOrOp(numberId[5]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            clickNumberOrOp(SQRT);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            sleep(500);
            clickNumberOrOp(numberId[5]);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (resultTextView.length() > 20) {
            assertEquals("test tool-021", "2.2360679774997896964091736", resultTextView);
        } else {
            assertEquals("test tool-021", "2.2360679774997", resultTextView);
        }
    }

    /**
     * tool-022
     * 1、打开计算器
     * 2、进行INV运算
     */
    @Test
    public void test_tool_022(){
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            clickNumberOrOp(INV);

            clickNumberOrOp(PER);
            clickNumberOrOp(ARCSIN);
            clickNumberOrOp(ARCCOS);
            clickNumberOrOp(ARCTAN);
            clickNumberOrOp(PI);
            clickNumberOrOp(EXP);
            clickNumberOrOp(FUN_POW);
            clickNumberOrOp(FACT);
            clickNumberOrOp(E);
            clickNumberOrOp(LPAREN);
            clickNumberOrOp(RPAREN);
            clickNumberOrOp(SQR);
            sleep(500);
            formulaTextView = formula.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickNumberOrOp(INV);

        mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
        clickDelOrClr(true);

        assertEquals("test tool-022", "%sin⁻¹(cos⁻¹(tan⁻¹(πexp(10^!e()²", formulaTextView);

    }

    /**
     * tool-023
     * 1、打开计算器
     * 2、进行 % 运算
     */
    @Test
    public void test_tool_023(){
        try {
            clickDelOrClr(true);
            clickNumberOrOp(numberId[8]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            clickNumberOrOp(PER);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);

        assertEquals("test tool-023", "0.08", resultTextView);

    }

    /**
     * test_tool_024
     * 1、选择INV
     * 2、进行sin∧-1运算
     */
    @Test
    public void test_tool_024() {
        String degOrRad = "RAD";
        clickDelOrClr(true);
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            clickNumberOrOp(INV);
            clickNumberOrOp(ARCSIN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(numberId[0]);
            clickNumberOrOp(POINT);
            clickNumberOrOp(numberId[5]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            clickNumberOrOp(RPAREN);

            clickNumberOrOp(INV);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(EQ);

            resultTextView = result.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (degOrRad.equals("RAD")) {
            assertEquals("test tool-024", "30", resultTextView);
        } else {
            if (resultTextView.length() > 20) {
                assertEquals("test tool-024", "0.5235987755982988730771072", resultTextView);
            } else {
                assertEquals("test tool-024", "0.5235987755982", resultTextView);
            }
        }
    }


    /**
     * test_tool_025
     * 1、选择INV
     * 2、进行cos∧-1运算
     */
    @Test
    public void test_tool_025() {
        String degOrRad = "RAD";
        clickDelOrClr(true);
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            clickNumberOrOp(INV);
            clickNumberOrOp(ARCCOS);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(numberId[0]);
            clickNumberOrOp(POINT);
            clickNumberOrOp(numberId[5]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            clickNumberOrOp(RPAREN);

            clickNumberOrOp(INV);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(EQ);

            resultTextView = result.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (degOrRad.equals("RAD")) {
            assertEquals("test tool-025", "60", resultTextView);
        } else {
            if (resultTextView.length() > 20) {
                assertEquals("test tool-025", "1.0471975511965977461542144", resultTextView);
            } else {
                assertEquals("test tool-025", "1.0471975511965", resultTextView);
            }
        }
    }

    /**
     * test_tool_026
     * 1、选择INV
     * 2、进行tan∧-1运算
     */
    @Test
    public void test_tool_026() {
        String degOrRad = "RAD";
        clickDelOrClr(true);
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            clickNumberOrOp(INV);
            clickNumberOrOp(ARCTAN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(numberId[1]);
//            clickNumberOrOp(POINT);
//            clickNumberOrOp(numberId[5]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            clickNumberOrOp(RPAREN);

            clickNumberOrOp(INV);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(EQ);

            resultTextView = result.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        if (degOrRad.equals("RAD")) {
            assertEquals("test tool-026", "45", resultTextView);
        } else {
            if (resultTextView.length() > 20) {
                assertEquals("test tool-026", "−0.988031624092861789987748", resultTextView);//TODO
            }
            assertEquals("test tool-026", "0.7853981633974", resultTextView);
        }

        assertEquals("test tool-026", "45", resultTextView);

    }

    /**
     * test_tool_027
     * 1、选择INV
     * 2、进行e∧x运算 e^5
     */
    @Test
    public void test_tool_027() {
        clickDelOrClr(true);
        try {
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            clickNumberOrOp(E);
            clickNumberOrOp(POW);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(numberId[5]);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);

        if (resultTextView.length() > 20) {
            assertEquals("test tool-027", "148.41315910257660342111558", resultTextView);//TODO
        } else {
            assertEquals("test tool-027", "148.41315910257", resultTextView);
        }
    }

    /**
     * test_tool_028
     * 1、选择INV
     * 2、进行 10∧x运算 10^5
     */
    @Test
    public void test_tool_028() {
        clickDelOrClr(true);
        try {
            clickNumberOrOp(numberId[1]);
            clickNumberOrOp(numberId[0]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            clickNumberOrOp(POW);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(numberId[5]);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);

        assertEquals("test tool-027", "100,000", resultTextView);
    }


    /**
     * test_tool_029
     * 1、选择INV
     * 2、进行 x∧2运算  3.6^2
     */
    @Test
    public void test_tool_029() {
        clickDelOrClr(true);
        try {
            clickNumberOrOp(numberId[3]);
            clickNumberOrOp(POINT);
            clickNumberOrOp(numberId[6]);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.LEFT, 1f);
            sleep(500);
            clickNumberOrOp(POW);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            clickNumberOrOp(numberId[2]);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        clickDelOrClr(true);
        assertEquals("test tool-027", "12.96", resultTextView);
    }

    /**
     * tool-030
     * 1、打开计算器
     * 2、进行tan DEG 显示运算 tan(45)
     */
    @Test
    public void test_tool_030(){
        clickDelOrClr(true);
        try {
            clickNumberOrOp(PAD_ADV);
            clickNumberOrOp(TAN);
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);//swipeLeft();
            //sleep(500);
            clickNumberOrOp(numberId[4]);
            clickNumberOrOp(numberId[5]);
            //sleep(500);
            clickNumberOrOp(PAD_ADV);
            sleep(500);
            clickNumberOrOp(RPAREN);

            String degOrRad = mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEG)).getText();
            if (!degOrRad.equals("DEG")) {
                clickNumberOrOp(DEG);
            }


            mDevice.findObject(By.res(TEST_PACKAGE_NAME, PAD_ADV)).swipe(Direction.RIGHT, 1f);
            //formulaTextView = formula.getText().trim();
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        if (resultTextView.length() > 20) {
            assertEquals("test tool-030", "1.6197751905438615499827965", resultTextView);
        } else {
            assertEquals("test tool-030", "1.6197751905438", resultTextView);
        }

    }

    /**
     * tool-031
     * 1、打开计算器
     * 2、进行 除法 运算
     */
    @Test
    public void test_tool_031(){
        clickDelOrClr(true);
        try {
            clickNumberOrOp(numberId[0]);
            clickNumberOrOp(DIV);

            clickNumberOrOp(numberId[randomNuID()]);
            sleep(500);
            clickNumberOrOp(EQ);
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        assertEquals("test tool-031", "0", resultTextView);

    }

    /**
     * tool-032
     * 1、打开计算器
     * 2、进行 x -
     */
    @Test
    public void test_tool_032(){
        clickDelOrClr(true);
        try {
            clickNumberOrOp(MUL);//x
            clickNumberOrOp(SUB);//-

            formulaTextView = formula.getText().trim();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        assertEquals("test tool-032", "−", formulaTextView);

    }

    /**
     * tool-033
     * 1、打开计算器
     * 2、进行 / -
     */
    @Test
    public void test_tool_033(){
        clickDelOrClr(true);
        try {
            clickNumberOrOp(DIV);// /
            clickNumberOrOp(SUB);//-

            formulaTextView = formula.getText().trim();
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        assertEquals("test tool-033", "−", formulaTextView);

    }


    /**
     * tool-034
     * 1、打开计算器
     * 2、进行 除法 x /0 运算
     */
    @Test
    public void test_tool_034(){
        clickDelOrClr(true);
        try {

            clickNumberOrOp(numberId[randomNuID()]);
            clickNumberOrOp(numberId[randomNuID()]);
            clickNumberOrOp(DIV);
            clickNumberOrOp(numberId[0]);
            sleep(500);
            clickNumberOrOp(EQ);

            formulaTextView = formula.getText().trim();
            resultTextView = result.getText().trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        assertEquals("test tool-034", "Can't divide by 0", resultTextView);

    }

    /**
     * tool-035
     * 1、打开计算器
     * 2、能够将光标的前一个数字或运算符逐一删除
     */
    @Test
    public void test_tool_035(){

        //int k = 0;
        int count = 0;
        int digits = (int)(Math.random() * 10) + 1;//产生至少一位数
        //Log.e(TAG, "digits===" + digits);
        clickDelOrClr(true);
        try {
            for(int i = 0; i < digits; i++) {
                clickNumberOrOp(numberId[randomNuID()]);
                //formulaTextView = formula.getText().trim();
            }

            sleep(1000);
            formulaTextView = formula.getText().trim();
            //Log.e(TAG, "forTextView.lenght==="+formulaTextView.length());

            for(int j = 0; j < formulaTextView.length(); j++){
                mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEL)).click();
                String formulaTvDel = formula.getText().trim();
                //Log.e(TAG, "formulaTvDel==="+formulaTvDel);
                count++;
                //Log.e(TAG, "count ==" + count);
                if (formulaTvDel.equals("")){
                    break;
                }
                //Log.e(TAG ,"k===="+k++ );
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        clickDelOrClr(true);

        assertEquals("test tool-035", digits, count);

    }

    /**
     * tool-036
     * 1、打开计算器
     * 2、点击手机不会报错，开放源代码许可界面显示正常
     */
    @Test
    public void test_tool_036(){
        UiObject colonContent;
        String webViewText = "";
        clickDelOrClr(true);
        try {
            //formulaTextView = formula.getText().trim();
            mDevice.findObject(By.res(TEST_PACKAGE_NAME, "formula")).click();// android.widget.ActionMenuView
            UiObject colon = mDevice.findObject(new UiSelector().className("android.widget.ImageButton"));/*("android.widget.ActionMenuView"));*/
            colon.click();

            //if (formulaTextView.equals("")) {
            colonContent = mDevice.findObject(new UiSelector().className("android.widget.RelativeLayout")//"android.widget.LinearLayout")
                        .index(0).childSelector(new UiSelector().text("Open source licenses"/*"History"*/)));
            colonContent.click();


            UiObject webView = mDevice.findObject(new UiSelector().className("android.widget.FrameLayout")
                    .index(0).childSelector(new UiSelector().textStartsWith("")));
            Log.e(TAG, "webView==="+webView);
            webViewText = webView.getText().trim();
            Log.e(TAG, "webViewText==="+webViewText);
            mDevice.pressHome();

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("test tool-036", "Open source licenses", webViewText);
    }

//    @After
//    public void test_end() {
//        mDevice.pressBack();
//        mDevice.pressHome();
//        try {
//            mDevice.pressRecentApps();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        mDevice.swipe();
//    }

/***************************************测试 CASE end**************************************************/




/*****************************************其他方法 begin**************************************************/

    /**
     * 进入allapps
     */
//    public  void enterAllApps() {
//        mDevice.findObject(By.res(LAUNCH_NAME, ALLAPPS)).click();
//        mDevice.wait(Until.hasObject(By.pkg(LAUNCH_NAME).depth(0)), LAUNCH_TIMEOUT);
//
//    }

    /**
     * number or op
     * @param id
     */
    public void clickNumberOrOp(String id){
        mDevice.findObject(By.res(TEST_PACKAGE_NAME, id)).click();
    }


    /**
     * 根据实际控件显示名 点击 DEL 或者 CLR
     * isLongClick 为 true 表示长按 否则短按
     * @param isLongClick
     */
    public void clickDelOrClr(boolean isLongClick) {
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UiObject clrordel = mDevice.findObject(new UiSelector().text("CLR"));
        //UiObject2 clrordel = new UiObject2(new UiSelector().text("CLR"));
        Log.e(TAG, "clrordel.exists==="+ clrordel.exists());
        if (clrordel.exists()) {
            try {
                clrordelText = clrordel.getText();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (isLongClick) {
            if (clrordelText.equals("CLR")) {
                mDevice.findObject(By.res(TEST_PACKAGE_NAME, CLR)).longClick();
            } else {
                mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEL)).longClick();
            }
        } else {
            if (clrordelText.equals("CLR")) {
                mDevice.findObject(By.res(TEST_PACKAGE_NAME, CLR)).click();
            } else {
                mDevice.findObject(By.res(TEST_PACKAGE_NAME, DEL)).click();
            }
        }
    }



    public void clickApp(String className, int dex, String appName) {
        try {
            UiObject appItem = mDevice.findObject(new UiSelector().className(className).index(dex).childSelector(new UiSelector().text(appName)));

            appItem.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开app
     * @param packageName
     * @param timeOut
     */
    public void startApp(String packageName, int timeOut){
        Context context = InstrumentationRegistry.getContext();

        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), timeOut);
    }

    /**
     * 点击号码
     * @param packageName
     * @param id
     */
    public void clickDialerNumberId(String packageName, int id){
        mDevice.findObject(By.res(packageName, numberId[id])).click();

    }
    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */

    public String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    /**
     * 随机产生1 - 9 的numberID
     */
    public int randomNuID(){
        int randomID = 1;
        int randomNum = (int)(Math.random() * 10);
        if (randomNum != 0 && randomNum != 10 ) {
            randomID = randomNum;
        }

        return randomID;
    }

/*****************************************其他方法 end**************************************************/
}
