package com.example.android.testing.uiautomator.SagerealBatteryKPI;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-3-11.
 ************************************************************************/
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SagerealBatteryKPIBefore.class,//测试前的准备类
//        test01.class
        SagerealBatteryKPI.class
})

public class TestSuit {
}
