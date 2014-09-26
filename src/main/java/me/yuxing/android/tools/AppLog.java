package me.yuxing.android.tools;

import android.util.Log;

/**
 * Created by yuxing on 2014-09-26.
 */
public class AppLog {

    public static void v(String tag, String msg) {
        Log.v(tag, msg == null ? "[null]" : msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg == null ? "[null]" : msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg == null ? "[null]" : msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg == null ? "[null]" : msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg == null ? "[null]" : msg);
    }

}
