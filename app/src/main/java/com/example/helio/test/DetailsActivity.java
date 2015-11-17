package com.example.helio.test;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


public class DetailsActivity extends AppCompatActivity implements ViewPager.PageTransformer {

    public static final String EXTRA_URL_INDEX = "EXTRA_URL_INDEX";

    private ViewPager viewPager;
    private ImageView pagerTopBgImageView;
    private ImageView pagerBottomBgImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.postponeEnterTransition(this);
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
        ImageView imageView = (ImageView) ((ViewGroup) page).getChildAt(0);
        float previewScale = 0.7f;
        float scaleDifference = 1f - previewScale;
        float positionAbs = Math.abs(position);
        float scale = 1 - (scaleDifference * positionAbs);
        float previewAlpha = 0.5f;
        float alphaDifference = 1f - previewAlpha;
        float alpha = 1 - (alphaDifference * positionAbs);
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        imageView.setAlpha(alpha);
        float translation = (page.getWidth() - (imageView.getWidth() * scale)) / 2f;
        imageView.setTranslationX(translation * -position);
        String leftPageBgUrl = (String) page.getTag(R.id.image_pager_bg_bottom);
        String rightPageBgUrl = (String) page.getTag(R.id.image_pager_bg_top);
        if (position >= -0.90 && position <= 0.10 && leftPageBgUrl != null && rightPageBgUrl != null) {
//      if (position >= -1 && position <= 0 && leftPageBgUrl != null && rightPageBgUrl != null) { para pager em ordem reversa
            if ((pagerTopBgImageView.getTag() != rightPageBgUrl && pagerBottomBgImageView.getTag() != rightPageBgUrl)
                    || (pagerTopBgImageView.getTag() != leftPageBgUrl && pagerBottomBgImageView.getTag() != leftPageBgUrl)) {
                updatePagerBgWithUrl(rightPageBgUrl, leftPageBgUrl);
                updatePagerBgWithUrl(leftPageBgUrl, rightPageBgUrl);
            }
            pagerTopBgImageView.setAlpha(pagerTopBgImageView.getTag() == rightPageBgUrl ? positionAbs : 1 - positionAbs);
        }
    }

    private void updatePagerBgWithUrl(String url, String dontOverrideUrl) {
        if (url != pagerTopBgImageView.getTag() && url != pagerBottomBgImageView.getTag()) {
            ImageView imageViewToOverride = (dontOverrideUrl == pagerTopBgImageView.getTag()) ? pagerBottomBgImageView : pagerTopBgImageView;
            loadWithBlurInto(url, imageViewToOverride);
            imageViewToOverride.setTag(url);
        }
    }
}
