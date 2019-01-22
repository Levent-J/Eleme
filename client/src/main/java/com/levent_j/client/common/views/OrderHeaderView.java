package com.levent_j.client.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Shop;
import com.levent_j.client.R;

/**
 * @auther : levent_j on 2018/3/28.
 * @desc :
 */

public class OrderHeaderView extends RelativeLayout{


    public OrderHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_header,this);

    }


}
