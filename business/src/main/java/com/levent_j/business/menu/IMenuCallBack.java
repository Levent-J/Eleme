package com.levent_j.business.menu;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Food;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/12.
 * @desc :
 */

public interface IMenuCallBack {
    void onLoadFoodsSuccess(List<Food> foods);
    void onLoadFoodsFailed(AVException e);
    void onAddFoodSuccess();
    void onAddFoodFailed();
    void onDelFoodSuccess();
    void onDelFoodFailed(AVException e);
    void onEditFoodSuccess();
    void onEditFoodFailed();
}
