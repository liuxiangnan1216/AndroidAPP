package com.example.lxn.agingtestnew.testItem;

import android.os.Handler;
import android.view.View;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class LightTestItem extends AbstractBaseTestItem{
    public LightTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        return false;
    }

    @Override
    protected void initView(View view) {

    }
}
