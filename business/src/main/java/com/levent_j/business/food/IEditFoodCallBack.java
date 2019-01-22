package com.levent_j.business.food;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Food;

/**
 * @auther : levent_j on 2018/3/13.
 * @desc :
 */

public interface IEditFoodCallBack {
    void onCreateFoodSuccess();
    void onCreateFoodFailed(AVException e);

    void onEditFoodSuccess();
    void onEditFoodFailed(AVException e);

}
