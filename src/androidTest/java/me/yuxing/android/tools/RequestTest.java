package me.yuxing.android.tools;

import android.app.Application;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

import me.yuxing.android.tools.http.Request;

/**
 * Created by yuxing on 2014-09-28.
 */
public class RequestTest extends ApplicationTestCase<Application> {
    public RequestTest() {
        super(Application.class);
    }

    public void testOne() throws InterruptedException, FileNotFoundException {
        Bundle params = new Bundle();
        params.putString("name", "test2");
        params.putParcelable("image", ParcelFileDescriptor.open(new File("/storage/sdcard1/img2.jpg"), ParcelFileDescriptor.MODE_READ_ONLY));
        new Request.Builder(getContext())
                .url("http://selfie.2pick1.com/test")
                .method(com.android.volley.Request.Method.POST)
                .post(params)
                .callback(new Request.Callback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("RequestTest", throwable.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("RequestTest", response);
                    }
                })
                .call();

        Thread.sleep(10000);
    }
}
