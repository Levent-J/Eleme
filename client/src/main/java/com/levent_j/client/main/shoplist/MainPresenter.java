package com.levent_j.client.main.shoplist;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.net.ApiService;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class MainPresenter extends BasePresenter{

    private IMainCallback mMainCallback;

    public MainPresenter(IMainCallback mMainCallback) {
        this.mMainCallback = mMainCallback;
    }

    public void loadShopList(){
        ApiService.loadShopList(new FindCallback<Shop>() {
            @Override
            public void done(List<Shop> list, AVException e) {
                if (e==null){
                    mMainCallback.onGetShopListSuccess(list);
                }else {
                    mMainCallback.onGetShopListFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
