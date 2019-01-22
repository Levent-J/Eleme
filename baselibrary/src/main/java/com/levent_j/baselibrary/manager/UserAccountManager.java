package com.levent_j.baselibrary.manager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.im.v2.AVIMOptions;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.SharedPreferenceUtil;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @auther : levent_j on 2018/3/9.
 * @desc :
 */

public class UserAccountManager {

    private static UserAccountManager mInstance;
    private User mUser;
    private Shop mShop;

    private UserAccountManager() {
    }

    public static UserAccountManager getInstance(){
        if (mInstance == null) {
            mInstance = new UserAccountManager();
        }
        return mInstance;
    }

    public Shop getmShop() {
        return mShop;
    }

    public void setmShop(Shop mShop) {
        this.mShop = mShop;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public String getUserId(BaseActivity activity){
        return SharedPreferenceUtil.getUserData(activity);
    }

    public void saveUserData(BaseActivity activity,User user){
        SharedPreferenceUtil.saveUserData(activity, user);
    }

    public boolean isLogged(BaseActivity activity){
        //从SP中检查
        String id = getUserId(activity);
        return id != null;
    }


}
