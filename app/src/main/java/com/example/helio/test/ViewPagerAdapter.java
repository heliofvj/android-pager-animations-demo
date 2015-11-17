package com.example.helio.test;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Stack;

/**
 * Created by helio on 17/08/15.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private final int transitionItemIndex;
    private final Stack<ViewGroup> recycledViews = new Stack<>();

    public ViewPagerAdapter(int transitionItemIndex) {
        this.transitionItemIndex = transitionItemIndex;
    }

    @Override
    public int getCount() {
        return Data.URLS.length;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Context context = collection.getContext();
        ViewGroup pagerItemView;
        if (recycledViews.empty()) {
            pagerItemView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pager_item, collection, false);
        } else {
            pagerItemView = recycledViews.pop();
        }
        ImageView imageView = (ImageView) pagerItemView.getChildAt(0);
        Picasso.with(context).load(Data.URLS[position]).into(imageView);
        collection.addView(pagerItemView, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (position == transitionItemIndex) {
                imageView.setTransitionName(context.getString(R.string.transition_album_cover));
                ActivityCompat.startPostponedEnterTransition((Activity) context);
            } else {
                imageView.setTransitionName(null);
            }
        }
        pagerItemView.setTag(R.id.image_pager_bg_bottom, Data.URLS[position]);
        pagerItemView.setTag(R.id.image_pager_bg_top, position < getCount() - 1 ? Data.URLS[position + 1] : null);
        return pagerItemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        recycledViews.add((ViewGroup) view);
    }

    @Override
    public boolean isViewFromObject(final View arg0, final Object arg1) {
        return arg0 == arg1;
    }

}
