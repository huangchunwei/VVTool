package com.mevv.vvtool.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mevv.vvtool.MainActivity;
import com.mevv.vvtool.R;
import com.mevv.vvtool.base.BaseActivity;
import com.mevv.vvtool.util.SceneManager;
import com.mevv.vvtool.util.TipUtil;
import com.mevv.vvtool.widget.newcarousel.CarouselView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VV on 2016/10/21.
 */

public class CarouselViewActivity extends BaseActivity {

    static String[] imageUris = {
            "https://pic4.zhimg.com/03b2d57be62b30f158f48f388c8f3f33_b.png",
            "https://pic1.zhimg.com/4373a4f045e5e9ae16ebd6a624bf6228_b.png",
            "https://pic2.zhimg.com/0364e17a1561f48793993d8bf1cdc785_b.png",
            "https://pic2.zhimg.com/55fa74ff3eba164ed1db2037df1a8311_b.png",
            "https://pic4.zhimg.com/5dc30569c06e7c6266c9809f6eb80a7b_b.jpg"
    };
    private CarouselView mCarouselView;
    private List<CarouselView.BannerInfo> mBannerInfos;


    @Override
    public int bindLayout() {
        return R.layout.activity_carousel_view;
    }

    @Override
    public void init() {
        mCarouselView = (CarouselView) findViewById(R.id.carouselView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("轮播图");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBannerInfos = new ArrayList<>();
        CarouselView.BannerInfo info;
        for (int i = 0; i < 5; i++) {
            info = new CarouselView.BannerInfo();
            info.img = imageUris[i];
            info.name = "测试" + i;
            mBannerInfos.add(info);
        }
        mCarouselView.setCarouselData(mBannerInfos);
        mCarouselView.setPicInitListener(new CarouselView.PicInitListener() {
            @Override
            public void onPicInit(ImageView imageView, String url, int position) {
                Glide.with(CarouselViewActivity.this).load(url).into(imageView);
            }
        });

        mCarouselView.setOnCarouselClickListener(new CarouselView.OnCarouselClickListener() {
            @Override
            public void onCarouselClick(CarouselView.BannerInfo bannerInfo, int position) {
                TipUtil.showToast(CarouselViewActivity.this, "位置:"+position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCarouselView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCarouselView != null) {
            mCarouselView.stop();
        }
    }


    public void test(View v){
        SceneManager.toScene(this, MainActivity.class, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCarouselView.stop();
        mCarouselView = null;
        System.gc();
    }
}
