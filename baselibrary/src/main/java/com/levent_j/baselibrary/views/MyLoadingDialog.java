package com.levent_j.baselibrary.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.levent_j.baselibrary.R;

/**
 * @auther : levent_j on 2018/4/19.
 * @desc :
 */
public class MyLoadingDialog extends Dialog{

    private TextView mLoadingTips;

    private String mTips;

    public MyLoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        mLoadingTips = findViewById(R.id.tv_loading);

        mLoadingTips.setText(mTips);
    }


    public void setmTips(String mTips) {
        this.mTips = mTips;
    }
}
