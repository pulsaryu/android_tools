package me.yuxing.android.tools.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

/**
 * Created by yuxing on 2014-09-26.
 */
public class RequestQueue {

    private static RequestQueue sInstance;
    private com.android.volley.RequestQueue mRequestQueue;

    private RequestQueue(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public synchronized static RequestQueue getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RequestQueue(context);
        }

        return sInstance;
    }

    public static void add(Context context, Request request) {
        getInstance(context).mRequestQueue.add(request);
    }

    public static void cancelAll(Context context, Object tag) {
        getInstance(context).mRequestQueue.cancelAll(tag);
    }
}
