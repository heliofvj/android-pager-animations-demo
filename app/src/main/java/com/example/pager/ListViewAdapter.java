package com.example.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by helio on 12/11/15.
 */
class ListViewAdapter extends ArrayAdapter {

    public ListViewAdapter(Context context) {
        super(context, R.layout.list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        Picasso.with(context).load(Data.URLS[position]).into((ImageView) ((ViewGroup) convertView).getChildAt(0));
        return convertView;
    }

    @Override
    public int getCount() {
        return Data.URLS.length;
    }
}
