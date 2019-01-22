package com.levent_j.business.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Seat;
import com.levent_j.business.R;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

public class MyEditSeatDialog extends Dialog {

    private TextView mTitle;
    private TextView mOk;
    private TextView mCancel;
    private RadioGroup mRadioGroup;

    private int mMode;
    private String mAddTitle = "添加";
    private String mSubTitle = "删除";
    private String mType = Seat.TYPE_SMALL;

    private ISelectCallback mCallback;

    public static final int MODE_ADD = 0;
    public static final int MODE_SUB = 1;

    public MyEditSeatDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_seat);

        mTitle = findViewById(R.id.tv_dialog_title);
        mOk = findViewById(R.id.tv_dialog_ok);
        mCancel = findViewById(R.id.tv_dialog_cancel);
        mRadioGroup = findViewById(R.id.rg_seat);

        mRadioGroup.check(R.id.rb_small);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_small:
                        mType = Seat.TYPE_SMALL;
                        break;
                    case R.id.rb_medium:
                        mType = Seat.TYPE_MEDIUM;
                        break;
                    case R.id.rb_large:
                        mType = Seat.TYPE_LARGE;
                        break;
                }
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode == MODE_ADD){
                    mCallback.onAdd(mType);
                }else {
                    mCallback.onSub(mType);
                }
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setConfirmedCallback(ISelectCallback callback) {
        this.mCallback = callback;
    }

    public void setMode(int mode){
        this.mMode = mode;
    }

    @Override
    public void show() {
        super.show();
        mTitle.setText(String.format("请选择要%s的座位类型",mMode==MODE_ADD?mAddTitle:mSubTitle));
    }

    public interface ISelectCallback {
        void onAdd(String type);
        void onSub(String type);
    }
}
