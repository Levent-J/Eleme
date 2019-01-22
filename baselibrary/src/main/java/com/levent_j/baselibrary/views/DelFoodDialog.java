package com.levent_j.baselibrary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.levent_j.baselibrary.data.Food;

/**
 * @auther : levent_j on 2018/3/14.
 * @desc :
 */

public class DelFoodDialog extends AlertDialog.Builder{


    public DelFoodDialog(Context context) {
        super(context);
    }

    public void deleteFood(Food food){

    }
    @Override
    public AlertDialog.Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        return super.setPositiveButton(text, listener);
    }
}
