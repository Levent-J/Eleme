package com.levent_j.business.food;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.ImageUtil;

import java.io.File;

/**
 * @auther : levent_j on 2018/3/13.
 * @desc :
 */

public class EditFoodPresenter extends BasePresenter {

    private IEditFoodCallBack mEditFoodCallBack;

    public EditFoodPresenter(IEditFoodCallBack mEditFoodCallBack) {
        this.mEditFoodCallBack = mEditFoodCallBack;
    }

    public void createFood(String foodName, double foodPrice, final String foodDesc, File mImageFile) {
        //创建食物
        ApiService.createFood(foodName, foodPrice, foodDesc, 0,
                UserAccountManager.getInstance().getmShop().getObjectId(),
                ImageUtil.File2Bytes(mImageFile),
                new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            mEditFoodCallBack.onCreateFoodSuccess();
                        } else {
                            mEditFoodCallBack.onCreateFoodFailed(e);
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {

    }

    public void editFood(String foodId, String name, double price, String desc, File mImageFile) {
        ApiService.editFood(foodId, name, price, desc,
                ImageUtil.File2Bytes(mImageFile),
                new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            mEditFoodCallBack.onEditFoodSuccess();
                        } else {
                            mEditFoodCallBack.onEditFoodFailed(e);
                        }
                    }
                });
    }
}
