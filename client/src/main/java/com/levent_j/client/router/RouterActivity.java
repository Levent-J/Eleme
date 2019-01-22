package com.levent_j.client.router;

import android.content.Intent;

import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.client.R;
import com.levent_j.client.order.MyMyOrderDetailActivity;

/**
 * @auther : levent_j on 2018/4/22.
 * @desc :
 */
public class RouterActivity extends BaseActivity{

    public static final String KEY_CHANNEL = "channel";
    public static final String KEY_DATA = "data";

    public static final int CHANNEL_ORDER_DETAIL = 0;


    @Override
    public int setLayout() {
        return R.layout.activity_router;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        int channel = intent.getIntExtra(KEY_CHANNEL,0);
        String data = intent.getStringExtra(KEY_DATA);
        finish();
        switch (channel){
            case CHANNEL_ORDER_DETAIL:

                MyMyOrderDetailActivity.openActivityFromList(this,data);

                break;
        }
    }
}
