package com.makoware.keyo.Other;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.makoware.keyo.R;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doman412 on 4/15/14.
 */
public class CustomAdapter<T> extends ArrayAdapter<T> {

    private List<T> list;
    private AdapterViewDelegate<T> delegate;

    public CustomAdapter(Context context, List<T> list) {
        super(context, android.R.layout.simple_list_item_1, list);
        this.list = list;
    }

    public CustomAdapter(Context context, List<T> list, AdapterViewDelegate delegate){
        super(context, android.R.layout.simple_list_item_1, list);
        this.list = list;
        this.delegate = delegate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(delegate!=null) {
            T obj = list.get(position);
            return delegate.getView(obj, position, convertView, parent);
        }
        return super.getView(position, convertView, parent);
    }

    public interface AdapterViewDelegate<T> {
        public View getView(T object, int position, View convertView, ViewGroup parent);
    }
}
