package com.teducn.cn.bmobtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2017/8/3.
 */

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    private Context context;
    private List<T> data;
    private LayoutInflater layoutInflater;

    public BaseAdapter(Context context, List<T> data) {
        super();
        setContext(context);
        setData(data);
        this.layoutInflater = LayoutInflater.from(context);
    }

    public final Context getContext() {
        return context;
    }

    public final void setContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        this.context = context;
    }

    public final List<T> getData() {
        return data;
    }

    public final void setData(List<T> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data = data;
    }

    public final LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
