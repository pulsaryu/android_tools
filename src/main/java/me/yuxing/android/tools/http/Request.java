package me.yuxing.android.tools.http;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import me.yuxing.android.tools.R;
import me.yuxing.android.tools.Util;

/**
 * Created by yuxing on 2014-09-28.
 */
public class Request extends com.android.volley.Request<String> {

    private final Response.Listener<String> mListener;
    private HttpEntity mHttpEntity;

    public Request(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public void post(Bundle post) {

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (String key : post.keySet()) {
            Object value = post.get(key);

            if (value instanceof ParcelFileDescriptor) {
                ParcelFileDescriptor fileDescriptor = (ParcelFileDescriptor) value;
                multipartEntityBuilder.addBinaryBody(key, new ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor), ContentType.DEFAULT_BINARY, "image.jpg");
            } else {
                multipartEntityBuilder.addTextBody(key, String.valueOf(value), ContentType.create("text/plan", "UTF-8"));
            }
        }

        mHttpEntity = multipartEntityBuilder.build();
    }

    @Override
    public String getBodyContentType() {

        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    public static class Builder {
        private final Context mContext;
        private String mUrl;
        private int mMethod = Method.GET;
        private Callback mCallback;
        private Bundle mPost = new Bundle();
        private Object mTag;

        public Builder(final Context context) {
            mContext = context;
        }

        public Builder url(String url) {
            mUrl = url;
            return this;
        }

        public Builder method(int method) {
            mMethod = method;
            return this;
        }

        public Builder post(Bundle bundle) {
            mPost.putAll(bundle);
            return this;
        }

        public Builder post(String key, String value) {
            mPost.putString(key, value);
            return this;
        }

        public Builder post(String key, int value) {
            mPost.putInt(key, value);
            return this;
        }

        public Builder post(String key, File file) throws FileNotFoundException {
            mPost.putParcelable(key, ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            return this;
        }

        public Builder callback(Callback callback) {
            mCallback = callback;
            return this;
        }

        public Builder setTag(Object tag) {
            mTag = tag;
            return this;
        }

        public Request build() {

            int method = mPost.isEmpty() ? mMethod : Method.POST;

            Request request = new Request(method, mUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Request", response + "");
                    if (mCallback != null) {
                        mCallback.onResponse(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("Request", volleyError.getMessage() + "");
                    if (mCallback != null) {
                        mCallback.onFailure(volleyError);
                    }
                }
            });
            request.setTag(mTag);
            if (method == Method.POST) {
                request.post(mPost);
            }
            return request;
        }

        public Request call() {
            Request request = build();
            if (Util.isNetworkConnected(mContext)) {
                RequestQueue.getInstance(mContext).add(mContext, request);
            } else if (mCallback != null) {
                mCallback.onFailure(new NetworkErrorException(mContext.getString(R.string.network_error)));
            }
            return request;
        }
    }

    public static interface Callback {
        public void onFailure(Throwable throwable);
        public void onResponse(String response);
    }
}
