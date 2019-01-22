package com.levent_j.business.login;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.SharedPreferenceUtil;

/**
 * @auther : levent_j on 2018/3/11.
 * @desc :
 */

public class LoginPresenter extends BasePresenter{

    private ILoginCallBack mCallBack;

    private static final String SMS_NAME = "亲爱的老公";
    private static final String SMS_OP = "寻找老婆";
    private static final int SMS_TTL = 1;

    public LoginPresenter(ILoginCallBack callBack) {
        this.mCallBack = callBack;
    }

    /**
     * 发送登录验证码
     * @param phone
     */
    public void sendLoginSmsCode(String phone){
        User.requestLoginSmsCodeInBackground(phone, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    MyLog.d("sendLoginSmsCode","存在手机号");
                    mCallBack.onSendLoginSMSCodeSuccess();
                }else {
                    MyLog.e("sendLoginSmsCode","异常："+e);
                    if (e.getCode()==213){
                        mCallBack.onPhoneNotExist();
                    }else {
                        mCallBack.onSendLoginSMSCodeFailed(e);
                    }
                }
            }
        });
    }

    /**
     * 发送注册验证码
     * @param phone
     */
    public void sendRegisterSmsCode(String phone){
        AVOSCloud.requestSMSCodeInBackground(phone,SMS_NAME,SMS_OP,SMS_TTL, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    MyLog.d("sendRegisterSmsCode","发送成功");
                    mCallBack.onSendRegisterSMSCodeSuccess();
                }else {
                    MyLog.e("sendRegisterSmsCode","异常："+e);
                    mCallBack.onSendRegisterSMSCodeFailed(e);
                }
            }
        });
    }

    /**
     * 登录/注册
     * @param phone
     * @param code
     */
    public void signUpOrLogin(String phone, String code) {
        User.signUpOrLoginByMobilePhoneInBackground(phone, code, User.class, new LogInCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                if (e==null){
                    //注册并登录成功
                    mCallBack.onSignUpOrLoginSuccess(user);
                    //本地保存一份
                    UserAccountManager.getInstance().saveUserData(((LoginActivity) mCallBack),user);
                    //全局持有一个
                    UserAccountManager.getInstance().setmUser(user);

                    MyLog.d("signUpOrLogin","成功");
                }else {
                    //失败
                    mCallBack.onSignUpOrLoginFailed(e);
                    MyLog.e("signUpOrLogin","失败");
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
