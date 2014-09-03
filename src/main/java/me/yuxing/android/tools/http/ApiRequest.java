package me.yuxing.android.tools.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxing on 2014-09-02.
 */
public class ApiRequest extends Request<String> {

    private final Response.Listener<String> mListener;

    public ApiRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
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
        mListener.onResponse(response);
    }

    public static class Builder {

        private final Context mContext;
        private int mMethod = Method.GET;
        private String mBaseUrl;
        private Object mTag;
        private Map<String, String> mParams = new HashMap<String, String>();

        public Builder(Context context, String baseUrl) {
            mContext = context;
            mBaseUrl = baseUrl;
        }

        public Builder setMethod(int method) {
            mMethod = method;
            return this;
        }

        public Builder setTag(Object tag) {
            mTag = tag;
            return this;
        }

        public Builder addParam(String key, String value) {
            mParams.put(key, value);
            return this;
        }

        public Builder addParams(Map<String, String> mParams) {
            mParams.putAll(mParams);
            return this;
        }

        public ApiRequest build() {
            String url;
            if (mMethod == Method.GET) {
                url = mBaseUrl + "?" + queryBuild(mParams);
            } else {
                url = mBaseUrl;
            }
            ApiRequest request = new ApiRequest(mMethod, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            request.setTag(mTag);
            return request;
        }

        private static String queryBuild(Map<String, String> params) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            try {
                for (String key : params.keySet()) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append("&");
                    }
                    sb.append(key).append("=");
                    String value = params.get(key);
                    if (TextUtils.isEmpty(value)) {
                        sb.append("");
                    } else {
                        sb.append(URLEncoder.encode(value, "UTF-8"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
