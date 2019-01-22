package com.levent_j.baselibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.baselibrary.R;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class MainTitleBar extends RelativeLayout{

    private TextView mTitle;
    private ImageView mBack;

    public MainTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_main_title_bar,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitle = findViewById(R.id.tv_title_text);
        mBack = findViewById(R.id.btn_title_back);
    }

    public void setTitle(String text){
        mTitle.setText(text);
    }

    public TextView getTitle(){
        return mTitle;
    }

    public ImageView getBackBtn(){
        return mBack;
    }
}
