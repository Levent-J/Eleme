package com.levent_j.business.shop;

import com.avos.avoscloud.AVException;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public interface ICreateCallBack {
    void onCreateSuccess();
    void onCreateFailed(Exception e);

    void onEditSuccess();
    void onEditFailed(AVException e);
}
