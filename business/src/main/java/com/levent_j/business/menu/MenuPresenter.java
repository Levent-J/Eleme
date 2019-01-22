package com.levent_j.business.menu;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.MyLog;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/12.
 * @desc :
 */

public class MenuPresenter extends BasePresenter{

    private IMenuCallBack mMenuCallBack;

    public MenuPresenter(IMenuCallBack mMenuCallBack) {
        this.mMenuCallBack = mMenuCallBack;
    }

    public void loadMenu(){
        String shopId =UserAccountManager.getInstance().getmShop().getObjectId();
        ApiService.loadFoodList(shopId
                , new FindCallback<Food>() {
                    @Override
                    public void done(List<Food> list, AVException e) {
                        if (e==null){
                            for (Food food : list) {
                                MyLog.d(""+food);
                            }
                            mMenuCallBack.onLoadFoodsSuccess(list);
                        }else {
                            mMenuCallBack.onLoadFoodsFailed(e);
                        }
                    }
                });
    }


    public void delFood(Food food){
        ApiService.deleteFood(food, new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    mMenuCallBack.onDelFoodSuccess();
                }else {
                    mMenuCallBack.onDelFoodFailed(e);
                }
            }
        });
    }

    public void editFood(Food food){

    }

    @Override
    public void onDestroy() {
    }
}
