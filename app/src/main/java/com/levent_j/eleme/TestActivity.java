package com.levent_j.eleme;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.utils.MyLog;

/**
 * @auther : levent_j on 2018/3/9.
 * @desc :
 */

public class TestActivity extends BaseActivity{
    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        // 测试 SDK 是否正常工作的代码
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","this is ELEME");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    MyLog.d("success!");
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

}
