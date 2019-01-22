package com.levent_j.client.main.shoplist;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.GetCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class DrawerPresenter extends BasePresenter{

    private IDrawerCallBack mDrawerCallBack;

    public DrawerPresenter(IDrawerCallBack callBack) {
        this.mDrawerCallBack = callBack;
    }

    public void loadUserData(String id){

        ApiService.loadUserData(id, new GetCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                if (e==null){
                    UserAccountManager.getInstance().setmUser(user);
                    mDrawerCallBack.onLoadUserDataSuccess(user);
                }else {
                    mDrawerCallBack.onLoadUserDataFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
