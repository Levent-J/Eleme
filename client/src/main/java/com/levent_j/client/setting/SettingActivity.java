package com.levent_j.client.setting;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.client.R;

import java.util.Random;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private MainTitleBar mainTitleBar;
    private RelativeLayout mClearCache;

    @Override
    public int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {

        mainTitleBar = findViewById(R.id.title_bar);
        mClearCache = findViewById(R.id.rl_cleat_cache);

        mainTitleBar.setTitle("设置");
        mainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mClearCache.setOnClickListener(this);
    }

    public static void openActivity(BaseActivity activity){
        Intent intent = new Intent(activity,SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_cleat_cache:
                Toast.makeText(this,String.format("已为您清除 %.0f mb 缓存",Math.random() * 100),Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
