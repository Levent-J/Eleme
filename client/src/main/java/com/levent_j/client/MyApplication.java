package com.levent_j.client;

import android.app.Application;

import com.avos.avoscloud.PushService;
import com.levent_j.baselibrary.base.BaseApplication;
import com.levent_j.client.main.shoplist.MainActivity;
import com.levent_j.client.order.MyMyOrderDetailActivity;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class MyApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        PushService.setDefaultPushCallback(this, MyMyOrderDetailActivity.class);
    }
}
