package com.levent_j.baselibrary.base;

import android.app.Application;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.utils.MyLog;

/**
 * @auther : levent_j on 2018/3/9.
 * @desc :
 */

public class BaseApplication extends Application {
    //LeanCloud APP ID
    private static final String APP_ID = "R9uvaDKgVjtHRG4JRorIAiji-gzGzoHsz";
    //LeanCloud APP KEY
    private static final String APP_KEY = "GSAyUhpmFCzd2VYsPn5VFM5J";

    @Override
    public void onCreate() {
        super.onCreate();

        AVUser.registerSubclass(User.class);
        AVObject.registerSubclass(Food.class);
        AVObject.registerSubclass(Shop.class);
        AVObject.registerSubclass(Order.class);
        AVObject.registerSubclass(Seat.class);
        AVUser.alwaysUseSubUserClass(User.class);

        //LeanCloud 的初始化
        AVOSCloud.initialize(this, APP_ID, APP_KEY);
        //LeanCloud 开启调试日志
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
        //推送
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    MyLog.d("installation success");
                }else {
                    MyLog.e("installation failed " + e.getMessage());
                }
            }
        });
    }
}
