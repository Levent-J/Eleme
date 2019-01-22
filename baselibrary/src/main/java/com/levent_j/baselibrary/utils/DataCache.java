package com.levent_j.baselibrary.utils;

import android.util.LongSparseArray;

/**
 * @auther : levent_j on 2018/4/4.
 * @desc :
 */

public class DataCache {
    private static LongSparseArray<Object > mData = new LongSparseArray<>();
    private static long mId = 0;

    public static long add(Object data){
        mData.put(mId,data);
        return mId++;
    }

    public static Object get(long id){
        return mData.get(id);
    }

    public static void remove(long id){
        mData.remove(id);
    }


}
