package com.example.helio.test;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ifttt.sparklemotion.Animation;
import com.ifttt.sparklemotion.Decor;
import com.ifttt.sparklemotion.Page;
import com.ifttt.sparklemotion.SparkleMotion;
import com.ifttt.sparklemotion.SparkleViewPagerLayout;
import com.ifttt.sparklemotion.animations.AlphaAnimation;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


public class DetailsActivity extends AppCompatActivity implements ViewPager.PageTransformer {

    public static final String EXTRA_URL_INDEX = "EXTRA_URL_INDEX";

    private ViewPager viewPager;
    private SparkleMotion sparkleMotion;
    private SparkleViewPagerLayout sparkleViewPagerLayout;

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

        loadWithBlurInto(Data.URLS[0], ((ImageView) findViewById(R.id.image_pager_bg)));
        sparkleViewPagerLayout = (SparkleViewPagerLayout) findViewById(R.id.view_pager_layout);
        sparkleMotion = SparkleMotion.with(sparkleViewPagerLayout);
        for (int i = 0; i < Data.URLS.length - 1; i++) {
            Animation animation = new AlphaAnimation(Page.singlePage(i), 0, 1);
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.pager_bg, sparkleViewPagerLayout, false);
            loadWithBlurInto(Data.URLS[i + 1], imageView);
            Decor decor = new Decor.Builder(imageView).setPage(Page.pageRange(i, i + 1)).behindViewPage().build();
            sparkleMotion.animate(animation).on(decor);
        }
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
    }
}
