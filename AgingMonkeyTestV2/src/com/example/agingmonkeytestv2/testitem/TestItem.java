package com.example.agingmonkeytestv2.testitem;

import android.os.Handler;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-10.
 ************************************************************************/


public interface TestItem {
    boolean execTest(Handler handler);

    void setUp();

    void tearDown();
}
