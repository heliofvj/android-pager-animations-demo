package com.example.pager;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


public class DetailsActivity extends AppCompatActivity implements ViewPager.PageTransformer {

    public static final String EXTRA_URL_INDEX = "EXTRA_URL_INDEX";

    private static final float PAGE_PREVIEW_SCALE = 0.7f;
    private static final float PAGE_PREVIEW_ALPHA = 0.5f;

    private ViewPager viewPager;
    private ImageView pagerTopBgImageView;
    private ImageView pagerBottomBgImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.postponeEnterTransition(this);
        }
        setContentView(R.layout.activity_details);
        int selectedItem = getIntent().getIntExtra(EXTRA_URL_INDEX, 0);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageTransformer(false, this);
        viewPager.setAdapter(new ViewPagerAdapter(selectedItem));
        viewPager.setCurrentItem(selectedItem);
        pagerTopBgImageView = (ImageView) findViewById(R.id.image_pager_bg_top);
        pagerBottomBgImageView = (ImageView) findViewById(R.id.image_pager_bg_bottom);
    }

    private void loadWithBlurInto(String url, ImageView view) {
        Picasso.with(this).load(url).transform(new BlurTransformation(this)).transform(new ColorFilterTransformation(0x55000000)).into(view);
    }

    @Override
    public void transformPage(View page, float position) {
        Log.d(getClass().getSimpleName(), "transformPage.position: " + position);
        ImageView imageView = (ImageView) ((ViewGroup) page).getChildAt(0);
        float positionAbs = Math.abs(position);

        float previewScaleDifference = 1f - PAGE_PREVIEW_SCALE;
        float scale = 1 - (previewScaleDifference * positionAbs);
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

        float previewAlphaDifference = 1f - PAGE_PREVIEW_ALPHA;
        float alpha = 1 - (previewAlphaDifference * positionAbs);
        imageView.setAlpha(alpha);

        float translation = (page.getWidth() - (imageView.getWidth() * scale)) / 2f;
        imageView.setTranslationX(translation * -position);

        ViewPagerAdapter.PageInfo pageInfo = ((ViewPagerAdapter)viewPager.getAdapter()).getPageInfoForPageView(page);
        // if (position >= -1 && position <= 0 && leftPageBgUrl != null && rightPageBgUrl != null) { for pagers in reverse order
        if (position >= -0.90 && position <= 0.10 && pageInfo.pageBgUrl != null && pageInfo.rightPageBgUrl != null) {
            if (!pagerBgIsLoadedWith(pageInfo.rightPageBgUrl, pageInfo.pageBgUrl)) {
                loadPagerBgWithUrl(pageInfo.rightPageBgUrl, pageInfo.pageBgUrl);
                loadPagerBgWithUrl(pageInfo.pageBgUrl, pageInfo.rightPageBgUrl);
            }
            pagerTopBgImageView.setAlpha(pagerTopBgImageView.getTag() == pageInfo.rightPageBgUrl ? positionAbs : 1 - positionAbs);
        }
    }

    private boolean pagerBgIsLoadedWith(String bgUrl1, String bgUrl2) {
        return (bgUrl1 == pagerTopBgImageView.getTag() || bgUrl1 == pagerBottomBgImageView.getTag()) && (bgUrl2 == pagerTopBgImageView.getTag() || bgUrl2 == pagerBottomBgImageView.getTag());
    }

    private void loadPagerBgWithUrl(String url, String skipUrl) {
        if (url != pagerTopBgImageView.getTag() && url != pagerBottomBgImageView.getTag()) {
            ImageView imageViewToOverride = (skipUrl == pagerTopBgImageView.getTag()) ? pagerBottomBgImageView : pagerTopBgImageView;
            loadWithBlurInto(url, imageViewToOverride);
            imageViewToOverride.setTag(url);
        }
    }
}
