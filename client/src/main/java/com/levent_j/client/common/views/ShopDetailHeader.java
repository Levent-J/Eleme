package com.levent_j.client.common.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.client.R;
import com.levent_j.client.common.util.MyCircleImgTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * @auther : levent_j on 2018/3/16.
 * @desc :
 */

public class ShopDetailHeader extends RelativeLayout{

    private TextView mName;
    private TextView mAddr;
    private TextView mPhone;
    private ImageView mBack;
    private ImageView mAvatar;

    public ShopDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_shop_detail_header,this);

        mBack = findViewById(R.id.iv_back);
        mName = findViewById(R.id.tv_shop_name);
        mAddr = findViewById(R.id.tv_shop_address);
        mPhone = findViewById(R.id.tv_shop_phone);
        mAvatar = findViewById(R.id.iv_shop_avatar);
    }

    public void initData(Shop shop){
        mName.setText(shop.getName());
        mAddr.setText(shop.getAddress());
        mPhone.setText(shop.getPhone());

//        会变卡变慢？
        byte[] bytes = shop.getAvatar();
        File file = ImageUtil.Bytes2File(bytes);
        Picasso.get().load(file).transform(new MyCircleImgTransformation(160,160)).into(mAvatar);
    }


    public ImageView getBackBtn(){

        return mBack;
    }


}
