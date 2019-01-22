package com.levent_j.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.User;

/**
 * @auther : levent_j on 2018/3/11.
 * @desc :
 */

public class SharedPreferenceUtil {
    private static final String SP_NAME = "test";

    private static final String KEY_USER_ID = "key_user_id";

    public static void saveUserData(BaseActivity activity, User user) {
        SharedPreferences sp = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_USER_ID,user.getObjectId());
        editor.apply();
    }

    public static String getUserData(BaseActivity activity){
        SharedPreferences sp = activity.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sp.getString(KEY_USER_ID,null);
    }
}
