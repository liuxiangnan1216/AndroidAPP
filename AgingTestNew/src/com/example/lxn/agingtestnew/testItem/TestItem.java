package com.example.lxn.agingtestnew.testItem;


import android.os.Handler;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/

public interface TestItem {
    boolean execTest(Handler handler);

    void setUp();

    void tearDown();
}
