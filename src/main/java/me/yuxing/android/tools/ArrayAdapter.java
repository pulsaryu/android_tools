package me.yuxing.android.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuxing on 2014-09-02.
 */
public abstract class ArrayAdapter<T> extends BaseAdapter {

    private final Context mContext;
    private List<T> mData = new ArrayList<T>();
    private final LayoutInflater mInflater;

    public ArrayAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItem(T item) {
        mData.add(item);
    }

    public void addItems(List<T> items) {
        mData.addAll(items);
    }

    public void addItems(T[] items) {
        mData.addAll(Arrays.asList(items));
    }

    public void remove(T item) {
        mData.remove(item);
    }

    public void clear() {
        mData.clear();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = newView(mInflater, viewGroup, i);
        }

        bindView(view, i);

        return view;
    }

    protected abstract View newView(LayoutInflater inflater, ViewGroup parent, int position);

    protected abstract void bindView(View view, int position);

}
