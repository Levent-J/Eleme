package com.levent_j.baselibrary.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @auther : levent_j on 2018/3/8.
 * @desc :
 */

public class MyLog {
    private static final String TAG_DEFAULT = "levent_j";
    private static final int TABS_SPACE_NUMS = 20;

    public static void d(String msg) {
        d(TAG_DEFAULT, msg);
    }

    public static void d(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT;
        }
        //前置一个空格
        msg=" "+msg;
//╔ ╦ ╗ ╣ ╬ ╠ ╚ ╩ ╝  ═

        //先得到目的行
        String trackLine = getTrackLine();
        //计算需要多少个制表符
        int tabsSize = Math.max(trackLine.length(), msg.length()) + TABS_SPACE_NUMS;
        //生成制表符字符串
        String tabs = createTabs(tabsSize);

        //打印第一行 纯制表符
        Log.d(tag, "╔" + tabs + "╗");
        //打印msg文本 补充空格 最后加一个制表符
        Log.d(tag, "║" + msg + createSpace(tabsSize - msg.length()) + "║");
        //打印分隔行 也是纯制表符
        Log.d(tag, "╠" + tabs + "╣");
        //打印跳转行  补充空格 最后加一个制表符
        Log.d(tag, "║" + trackLine + createSpace(tabsSize - trackLine.length()) + "║");
        //最后一行 纯制表符
        Log.d(tag, "╚" + tabs + "╝");
    }

    public static void e(String msg) {
        e(TAG_DEFAULT, msg);
    }

    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT;
        }
        //前置一个空格
        msg=" "+msg;
//╔ ╦ ╗ ╣ ╬ ╠ ╚ ╩ ╝  ═

        //先得到目的行
        String trackLine = getTrackLine();
        //计算需要多少个制表符
        int tabsSize = Math.max(trackLine.length(), msg.length()) + TABS_SPACE_NUMS;
        //生成制表符字符串
        String tabs = createTabs(tabsSize);

        //打印第一e行 纯制表符
        Log.e(tag, "╔" + tabs + "╗");
        //打印msg文本 补充空格 最后加一个制表符
        Log.e(tag, "║" + msg + createSpace(tabsSize - msg.length()) + "║");
        //打印分隔行 也是纯制表符
        Log.e(tag, "╠" + tabs + "╣");
        //打印跳转行  补充空格 最后加一个制表符
        Log.e(tag, "║" + trackLine + createSpace(tabsSize - trackLine.length()) + "║");
        //最后一行 纯制表符
        Log.e(tag, "╚" + tabs + "╝");
    }

    private static String createTabs(int tabsSize) {
        return createStringBySameChar("═", tabsSize);
    }

    private static String createSpace(int spaceSize) {
        return createStringBySameChar(" ", spaceSize);
    }

    private static String createStringBySameChar(String ch, int size) {
        StringBuffer stringBuffer = new StringBuffer();
        while (size > 0) {
            size--;
            stringBuffer.append(ch);
        }
        return stringBuffer.toString();
    }

    private static String getTrackLine() {
        StackTraceElement traceElement = null;
        boolean shouldTrack = false;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            boolean isLogMethod = element.getClassName().equals(MyLog.class.getName());
            if (shouldTrack && !isLogMethod) {
                traceElement = element;
                break;
            }
            shouldTrack = isLogMethod;
        }

        String result = " at " + traceElement.getClassName() + "." + traceElement.getMethodName() + "(" + traceElement.getFileName() + ":" + traceElement.getLineNumber() + ")";
        return result;
    }


}
