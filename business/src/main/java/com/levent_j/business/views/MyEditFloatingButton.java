package com.levent_j.business.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.levent_j.business.R;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

public class MyEditFloatingButton extends RelativeLayout implements View.OnClickListener {
    private ImageView mSeatEditCloesd;
    private ImageView mSeatEditOpened;
    private ImageView mSeatAdd;
    private ImageView mSeatDel;

    private int mode = MODE_CLOSED;

    private Context mContext;
    private IEditFabCallback mCallback;

    private static final int MODE_CLOSED = 0;
    private static final int MODE_OPENED = 1;


    public MyEditFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_my_fab, this);

        mContext = context;

        mSeatEditCloesd = findViewById(R.id.btn_edit_closed);
        mSeatEditOpened = findViewById(R.id.btn_edit_opened);
        mSeatAdd = findViewById(R.id.btn_add);
        mSeatDel = findViewById(R.id.btn_del);

        mSeatEditCloesd.setOnClickListener(this);
        mSeatEditOpened.setOnClickListener(this);
        mSeatAdd.setOnClickListener(this);
        mSeatDel.setOnClickListener(this);
    }

    public void setCallBack(IEditFabCallback callBack) {
        this.mCallback = callBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_closed:
                //打开两个按钮
                mSeatAdd.setVisibility(VISIBLE);
                mSeatDel.setVisibility(VISIBLE);

                mSeatEditOpened.setVisibility(VISIBLE);

                Animation animAdd = AnimationUtils.loadAnimation(mContext,R.anim.anim_fab_top);
                mSeatAdd.startAnimation(animAdd);
                Animation animDel = AnimationUtils.loadAnimation(mContext,R.anim.anim_fab_left);
                mSeatDel.startAnimation(animDel);
                Animation animClose = AnimationUtils.loadAnimation(mContext,R.anim.anim_fab_close);
                mSeatEditCloesd.startAnimation(animClose);
                animClose.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mSeatEditCloesd.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case R.id.btn_edit_opened:
                //收起两个按钮
                mSeatAdd.setVisibility(GONE);
                mSeatDel.setVisibility(GONE);
                mSeatEditCloesd.setVisibility(VISIBLE);
                mSeatEditOpened.setVisibility(GONE);

                Animation animAddExit = AnimationUtils.loadAnimation(mContext,R.anim.anim_fab_top_exit);
                mSeatAdd.startAnimation(animAddExit);
                Animation animDelExit = AnimationUtils.loadAnimation(mContext,R.anim.anim_fab_left_exit);
                mSeatDel.startAnimation(animDelExit);
                Animation animOpen = AnimationUtils.loadAnimation(mContext,R.anim.anim_fab_open);
                mSeatEditCloesd.startAnimation(animOpen);

                break;
            case R.id.btn_add:
                mCallback.onClickAdd();
                break;
            case R.id.btn_del:
                mCallback.onClickDel();
                break;
        }
    }

    public interface IEditFabCallback {
        void onClickAdd();

        void onClickDel();
    }
}
