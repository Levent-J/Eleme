package com.levent_j.business.info;

import android.content.Context;
import android.content.Intent;

import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.business.R;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class InfoActivity extends BaseActivity {
    @Override
    public int setLayout() {
        return R.layout.activity_info;
    }

    @Override
    public void init() {

    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, InfoActivity.class);
        context.startActivity(intent);
    }
}