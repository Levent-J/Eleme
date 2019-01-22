package com.levent_j.baselibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.baselibrary.R;

/**
 * @auther : levent_j on 2018/4/19.
 * @desc :
 */
public class MyEmptyView extends RelativeLayout {

    private TextView mLoading;
    private TextView mEmpty;
    private TextView mRetry;

    public static final int MODE_LOADING = 0;
    public static final int MODE_EMPTY = 1;
    public static final int MODE_RETRY = 2;

    public MyEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_empty, this);

        mLoading = findViewById(R.id.tv_loading);
        mEmpty = findViewById(R.id.tv_empty);
        mRetry = findViewById(R.id.tv_retry);

    }

    public void setRetryCallvack(OnClickListener clickListener) {
        mRetry.setOnClickListener(clickListener);
    }

    public void setEmptyContent(String text) {
        mEmpty.setText(text);
    }

    public void setMode(int mode){

        mLoading.setVisibility(GONE);
        mEmpty.setVisibility(GONE);
        mRetry.setVisibility(GONE);

        switch (mode){
            case MODE_LOADING:
                mLoading.setVisibility(VISIBLE);
                break;
            case MODE_EMPTY:
                mEmpty.setVisibility(VISIBLE);
                break;
            case MODE_RETRY:
                mRetry.setVisibility(VISIBLE);
                break;
        }
    }
}
