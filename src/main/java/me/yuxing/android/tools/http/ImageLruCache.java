package me.yuxing.android.tools.http;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by yuxing on 2014-09-03.
 */
public class ImageLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public ImageLruCache(int maxSize) {
        super(maxSize);
    }

    public ImageLruCache() {
        super((int) (Runtime.getRuntime().maxMemory() >> 11));
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return (value.getRowBytes() * value.getHeight()) >> 10;
    }

    @Override
    public Bitmap getBitmap(String key) {
        return get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap value) {
        put(key, value);
    }
}
