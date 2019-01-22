package com.levent_j.business.order.orderdetail;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.business.R;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public class OrderControlView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout mBookOrder;
    private TextView mBookStatus;
    private LinearLayout mFoodOrder;
    private LinearLayout mStatus0Parent;
    private LinearLayout mStatus1Parent;
    private LinearLayout mStatus2Parent;
    private TextView mStatus0TV;
    private TextView mStatus1TV;
    private TextView mStatus2TV;
    private ImageView mStatus0IV;
    private ImageView mStatus1IV;
    private ImageView mStatus2IV;
    private ImageView mArrow0;
    private ImageView mArrow1;

    private Context mContext;
    private IOrderControlCallback mCallback;

    public OrderControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        inflate(context, R.layout.view_order_detail_control, this);
        mBookOrder = findViewById(R.id.rl_order_book);
        mBookStatus = findViewById(R.id.tv_order_book_status);
        mFoodOrder = findViewById(R.id.ll_order_food);
        mStatus0Parent = findViewById(R.id.ll_status0);
        mStatus1Parent = findViewById(R.id.ll_status1);
        mStatus2Parent = findViewById(R.id.ll_status2);
        mStatus0TV = findViewById(R.id.tv_status_confirmed);
        mStatus1TV = findViewById(R.id.tv_status_wait);
        mStatus2TV = findViewById(R.id.tv_status_eat);
        mStatus0IV = findViewById(R.id.iv_status_confirmed);
        mStatus1IV = findViewById(R.id.iv_status_wait);
        mStatus2IV = findViewById(R.id.iv_status_eat);
        mArrow0 = findViewById(R.id.iv_arrow0);
        mArrow1 = findViewById(R.id.iv_arrow1);


        mStatus0TV.setText("待确认");
        mStatus0TV.setTextColor(context.getResources().getColor(R.color.color_gray));
        mStatus0IV.setImageDrawable(context.getResources().getDrawable(R.drawable.wait_gray));

        mArrow0.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_gray));

        mStatus1TV.setText("");
        mStatus1TV.setTextColor(context.getResources().getColor(R.color.color_gray));
        mStatus1IV.setImageDrawable(context.getResources().getDrawable(R.drawable.wait_gray));

        mArrow1.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_gray));

        mStatus2TV.setText("");
        mStatus2TV.setTextColor(context.getResources().getColor(R.color.color_gray));
        mStatus2IV.setImageDrawable(context.getResources().getDrawable(R.drawable.wait_gray));

        mStatus0Parent.setClickable(false);
        mStatus1Parent.setClickable(false);
        mStatus2Parent.setClickable(false);

        mStatus0Parent.setOnClickListener(this);
        mStatus1Parent.setOnClickListener(this);
        mStatus2Parent.setOnClickListener(this);
    }

    public void setCallBack(IOrderControlCallback callBack) {
        this.mCallback = callBack;
    }

    public void setStatus(Order order,int status) {
        if (order.getType() == Order.TYPE_BOOK){
            mBookOrder.setVisibility(VISIBLE);
            mFoodOrder.setVisibility(GONE);

            Pair<String,String> pair = order.getTextByStatus();
            mBookStatus.setText(pair.first);
            mBookStatus.setTextColor(Color.parseColor(pair.second));
        }else {
            mBookOrder.setVisibility(GONE);
            mFoodOrder.setVisibility(VISIBLE);
            MyLog.d("status " + status);
            switch (status) {
                case Order.STATUS_ORIGIN://已下单 待确认
                    mStatus0Parent.setClickable(true);
                    mStatus1Parent.setClickable(false);
                    mStatus2Parent.setClickable(false);
                    break;
                case Order.STATUS_CONFIRMED://已确认 备餐中
                    changeStatus0();
                    mStatus0Parent.setClickable(false);
                    mStatus1Parent.setClickable(true);
                    mStatus2Parent.setClickable(false);
                    break;
                case Order.STATUS_WAIT://备餐完毕 待用餐
                    changeStatus0();
                    changeStatus1();
                    mArrow0.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_white));
                    mStatus0Parent.setClickable(false);
                    mStatus1Parent.setClickable(false);
                    mStatus2Parent.setClickable(true);
                    break;
                case Order.STATUS_FINISHED://用餐结束 待完成
                    changeStatus0();
                    changeStatus1();
                    changeStatus2();
                    mArrow0.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_white));
                    mArrow1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_white));
                    mStatus0Parent.setClickable(false);
                    mStatus1Parent.setClickable(false);
                    mStatus2Parent.setClickable(false);
                    break;
                case Order.STATUS_CANCELED:
                    break;
            }

        }

    }

    public void changeStatus0() {
        mStatus0TV.setText("已确认");
        mStatus0TV.setTextColor(mContext.getResources().getColor(R.color.color_text_white));
        mStatus0IV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.confirmed));
        mStatus1TV.setText("备餐中");
    }

    public void changeStatus1() {
        mStatus1TV.setText("备餐完毕");
        mStatus1TV.setTextColor(mContext.getResources().getColor(R.color.color_text_white));
        mStatus1IV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.confirmed));

        mStatus2TV.setText("待用餐");
    }

    public void changeStatus2() {
        mStatus2TV.setText("已完成");
        mStatus2TV.setTextColor(mContext.getResources().getColor(R.color.color_text_white));
        mStatus2IV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.confirmed));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_status0:
                mCallback.onClick0();
                break;
            case R.id.ll_status1:
                mCallback.onClick1();
                break;
            case R.id.ll_status2:
                mCallback.onClick2();
                break;
        }
    }

    public interface IOrderControlCallback {
        void onClick0();

        void onClick1();

        void onClick2();
    }
}
