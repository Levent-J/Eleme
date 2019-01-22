package com.levent_j.client.shopping.menu;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.net.ApiService;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/16.
 * @desc :
 */

public class ShopMenuPresenter extends BasePresenter{

    private IMenuCallback mTradeCallback;

    public ShopMenuPresenter(IMenuCallback mTradeCallback) {
        this.mTradeCallback = mTradeCallback;
    }

    /**
     * 获取商家信息
     * @param shopId
     */
    public void loadShopDetail(String shopId){
        ApiService.getShopDetail(shopId, new GetCallback<Shop>() {
            @Override
            public void done(Shop shop, AVException e) {
                if (e==null){
                    mTradeCallback.onLoadShopDetailSuccess(shop);
                }else {
                    mTradeCallback.onLoadShopDetailFailed(e);
                }
            }
        });
    }

    /**
     * 获取菜单
     * @param shopId
     */
    public void loadMenu(String shopId){
        ApiService.loadFoodList(shopId, new FindCallback<Food>() {
            @Override
            public void done(List<Food> list, AVException e) {
                if (e==null){
                    mTradeCallback.onLoadMenuSuccess(list);
                }else {
                    mTradeCallback.onLoadMenuFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
