package com.levent_j.business.main;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class MainPresenter extends BasePresenter{

    private MainCallBack mCallBack;

    public MainPresenter(MainCallBack mainCallBack) {
        mCallBack = mainCallBack;
    }

    /**
     * 获取商家数据
     */
    public void loadShopData(){
        String masterId = UserAccountManager.getInstance().getmUser().getObjectId();

        ApiService.queryShop(masterId,new GetCallback<Shop>() {
            @Override
            public void done(Shop shop, AVException e) {
                if (e==null){
                    mCallBack.onGetShopSuccess(shop);
                }else {
                    if (e.getCode()==101){
                        mCallBack.onGetShopSuccess(null);
                    }else {
                        mCallBack.onGetShopFailed(e);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }

    /**
     * 获取用户数据
     */
    public void loadUserData(String objId) {

        ApiService.loadUserData(objId,new GetCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                if (e==null){
                    //获取成功之后 应该保存本地User
                    UserAccountManager.getInstance().setmUser(user);
                    mCallBack.onGetUserDataSuccess();
                }else {
                    mCallBack.onGetUSerDataFailed(e);
                }
            }
        });
    }
}
