package com.levent_j.client.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Food;
import com.levent_j.client.R;

/**
 * @auther : levent_j on 2018/3/16.
 * @desc :
 */

public class FoodCountView extends RelativeLayout {

    private ImageView mSub;
    private TextView mCount;
    private ImageView mAdd;

    private int count = 0;
    private Food food;
    private ICountClickCallback mCallback;

    public FoodCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_food_count, this);

        mSub = findViewById(R.id.iv_sub);
        mCount = findViewById(R.id.tv_count);
        mAdd = findViewById(R.id.iv_add);

        mSub.setVisibility(GONE);
        mCount.setVisibility(GONE);


    }

    public void setFood(Food f){
        this.food = f;
    }


    public void setClickCallback(final ICountClickCallback callback) {
        mCallback = callback;
    }

    public void build(){
        mAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;
                mCallback.onAdd(food,count);
                refreshCounts();
            }
        });

        mSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                mCallback.onSub(food,count);
                refreshCounts();
            }
        });
    }

    private void refreshCounts() {
        if (count==0){
            mSub.setVisibility(GONE);
            mCount.setVisibility(GONE);
        }else {
            mSub.setVisibility(VISIBLE);
            mCount.setVisibility(VISIBLE);
            mCount.setText(""+count);
        }
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
        refreshCounts();
    }

    public interface ICountClickCallback {
        void onAdd(Food food, int result);

        void onSub(Food food, int result);
    }


}
