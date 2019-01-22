package com.levent_j.business.login;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.User;

/**
 * @auther : levent_j on 2018/3/11.
 * @desc :
 */

public interface ILoginCallBack {
    void onSendLoginSMSCodeSuccess();
    void onPhoneNotExist();
    void onSendLoginSMSCodeFailed(AVException e);

    void onSendRegisterSMSCodeSuccess();
    void onSendRegisterSMSCodeFailed(AVException e);

    void onSignUpOrLoginSuccess(User user);
    void onSignUpOrLoginFailed(AVException e);
}
