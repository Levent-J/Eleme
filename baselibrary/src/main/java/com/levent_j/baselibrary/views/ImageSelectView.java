package com.levent_j.baselibrary.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.levent_j.baselibrary.R;

/**
 * @auther : levent_j on 2018/4/3.
 * @desc :
 */

public class ImageSelectView extends RelativeLayout{

    private ImageView mCover;
    private ImageView mSrc;
    private ImageView mCha;

    public ImageSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_img_select,this);

        mCover = findViewById(R.id.iv_cover);
        mSrc = findViewById(R.id.iv_src);
        mCha = findViewById(R.id.iv_cha);

        //初始化
        mCover.setVisibility(VISIBLE);
        mSrc.setVisibility(GONE);
        mCha.setVisibility(GONE);
    }

    public ImageView getImageView(){
        return mSrc;
    }

    public void setImgMode(){
        mCover.setVisibility(GONE);
        mSrc.setVisibility(VISIBLE);
        mCha.setVisibility(VISIBLE);
    }

    public void setCoverMode(){
        mCover.setVisibility(VISIBLE);
        mSrc.setVisibility(GONE);
        mCha.setVisibility(GONE);
    }

    public void setChaCallback(OnClickListener chaCallback){
        mCha.setOnClickListener(chaCallback);
    }

    public void setCoverCallback(OnClickListener callback){
        mCover.setOnClickListener(callback);
    }




}
