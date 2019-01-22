package com.levent_j.baselibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @auther : levent_j on 2018/4/20.
 * @desc :
 */
public class ToastUtils {
    public static void avException(Context context,int code){
        switch (code){
            case 0:
                Toast.makeText(context,"网络超时，请重试",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
