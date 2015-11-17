package com.example.pager;

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
    private final Stack<View> recycledViews = new Stack<>();
    private boolean startedPostponedEnterTransition = false;

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
        ViewGroup pagerItemView = (ViewGroup) (recycledViews.empty() ? inflatePageView(collection) : recycledViews.pop());
        ImageView imageView = (ImageView) pagerItemView.getChildAt(0);
        Picasso.with(context).load(Data.URLS[position]).into(imageView);
        collection.addView(pagerItemView, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (position == transitionItemIndex) {
                imageView.setTransitionName(context.getString(R.string.transition_picture));
                if (!startedPostponedEnterTransition) {
                    startedPostponedEnterTransition = true;
                    ActivityCompat.startPostponedEnterTransition((Activity) context);
                }
            } else {
                imageView.setTransitionName(null);
            }
        }
        pagerItemView.setTag(new PageInfo(Data.URLS[position], position < getCount() - 1 ? Data.URLS[position + 1] : null));
        return pagerItemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        recycledViews.add((View) view);
    }

    @Override
    public boolean isViewFromObject(final View arg0, final Object arg1) {
        return arg0 == arg1;
    }

    private View inflatePageView(ViewGroup collection) {
        return LayoutInflater.from(collection.getContext()).inflate(R.layout.pager_item, collection, false);
    }

    public PageInfo getPageInfoForPageView(View view) {
        return (PageInfo) view.getTag();
    }

    public static class PageInfo {

        public final String pageBgUrl;
        public final String rightPageBgUrl;

        public PageInfo(String pageBgUrl, String rightPageBgUrl) {
            this.pageBgUrl = pageBgUrl;
            this.rightPageBgUrl = rightPageBgUrl;
        }

    }

}
