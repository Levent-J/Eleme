package com.levent_j.business.seat.seatdetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.business.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @auther : levent_j on 2018/4/5.
 * @desc :
 */

public class MySeatCodeDialog extends Dialog {

    private TextView mNo;
    private TextView mStatus;
    private ImageView mCodeImageView;

    private Bitmap mCodeBitmap;
    private Seat mSeat;

    private Context mContext;

    public MySeatCodeDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_seat_detail);

        mNo = findViewById(R.id.tv_no);
        mStatus = findViewById(R.id.tv_status);
        mCodeImageView = findViewById(R.id.iv_code);

        mNo.setText(mSeat.getType() + " " + mSeat.getNumber());

        Pair<String, String> pair = mSeat.getTextByStatus();
        mStatus.setText(pair.first);
        mStatus.setTextColor(Color.parseColor(pair.second));

        mCodeImageView.setImageBitmap(mCodeBitmap);

        mCodeImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageUtil.storeImage(mContext, mCodeBitmap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                return false;
            }
        });
    }

    public void setCodeBitmap(Bitmap bitmap) {
        this.mCodeBitmap = bitmap;
    }

    public void setSeat(Seat seat) {
        this.mSeat = seat;
    }
}
