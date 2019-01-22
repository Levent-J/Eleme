package com.levent_j.client.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.client.R;
import com.levent_j.client.main.shoplist.MainActivity;


/**
 * @auther : levent_j on 2018/3/9.
 * @desc :
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginCallBack {
    private EditText mPhoneNumber;
    private Button mNext;
    private EditText mVeriCode;
    private Button mSign;
    private TextView mTitle;
    private MainTitleBar mTitleBar;

    private LoginPresenter mLoginPresenter;

    private String phone;
    private String code;

    private static final int PANEL_PHONE = 0;
    private static final int PANEL_VERI = 1;

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        mTitleBar = findViewById(R.id.title_bar);
        mPhoneNumber = findViewById(R.id.et_login_phone);
        mNext = findViewById(R.id.btn_login_next);
        mVeriCode = findViewById(R.id.et_login_ver_code);
        mSign = findViewById(R.id.btn_login_sign);
        mTitle = findViewById(R.id.tv_login_title);

        mNext.setOnClickListener(this);
        mSign.setOnClickListener(this);

        //先进入 输入手机号模式
        changePanel(PANEL_PHONE);

        initTitleBar();

        mLoginPresenter = new LoginPresenter(this);

        addPresenter(mLoginPresenter);
    }

    private void initTitleBar() {
        mTitleBar.setTitle("");
        mTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePanel(PANEL_PHONE);
            }
        });
    }

    //改变面板
    private void changePanel(int status){
        mPhoneNumber.setVisibility(View.GONE);
        mNext.setVisibility(View.GONE);
        mVeriCode.setVisibility(View.GONE);
        mSign.setVisibility(View.GONE);

        if (status==PANEL_PHONE){
            mPhoneNumber.setVisibility(View.VISIBLE);
            mNext.setVisibility(View.VISIBLE);
            mTitleBar.getBackBtn().setVisibility(View.GONE);
        }else {
            mVeriCode.setVisibility(View.VISIBLE);
            mSign.setVisibility(View.VISIBLE);
            mTitleBar.getBackBtn().setVisibility(View.VISIBLE);
        }
    }


    // 启动Activity
    public static void openActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_next:
                phone = mPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                //发送登录验证码
                mLoginPresenter.sendLoginSmsCode(phone);
                break;
            case R.id.btn_login_sign:
                code = mVeriCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                //登录/注册
                mLoginPresenter.signUpOrLogin(phone,code);

        }
    }

    @Override
    public void onSendLoginSMSCodeSuccess() {
        //进入 验证码模式
        changePanel(PANEL_VERI);
    }

    @Override
    public void onPhoneNotExist() {
        //不存在手机号 开始注册
        //发送注册验证码
        mLoginPresenter.sendRegisterSmsCode(phone);

    }

    @Override
    public void onSendLoginSMSCodeFailed(AVException e) {
        //网络超时
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendRegisterSMSCodeSuccess() {
        //进入 验证码模式
        changePanel(PANEL_VERI);
    }

    @Override
    public void onSendRegisterSMSCodeFailed(AVException e) {
        //网络超时
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpOrLoginSuccess(User user) {
        MyLog.d("user:" + user);

        //登录成功 打开MainActivity
        MainActivity.openActivity(this);
        finish();
    }

    @Override
    public void onSignUpOrLoginFailed(AVException e) {
        //网络超时
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }


}
