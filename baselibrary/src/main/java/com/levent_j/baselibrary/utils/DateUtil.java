package com.levent_j.baselibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther : levent_j on 2018/3/24.
 * @desc :
 */

public class DateUtil {
    public static String Date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
