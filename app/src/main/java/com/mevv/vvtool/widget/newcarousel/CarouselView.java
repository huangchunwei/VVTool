package com.mevv.vvtool.widget.newcarousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mevv.vvtool.R;
import com.mevv.vvtool.widget.carousel.ZoomOutPageTransformer;

import java.util.List;

/**
 * Created by VV on 2016/9/26.
 */

public class CarouselView extends RelativeLayout {


    private final int DEFAULT_AUTO_CAROUSEL_TIMEMS = 3000;//ms
    private final int DEFAULT_HEIGHT = 175;//dp
    private final int DEFAULT_INDICATOR_SPACE_PADDING = 5;//dp
    private final int DEFAULT_INDICATOR_MARGIN_RIGHT = 17;//dp
    private final int DEFAULT_BOTTOM_BAR_HEIGHT = 30;//dp
    private final int DEFAULT_PIC_DESC_MARGIN_LEFT = 15;//dp
    private final int DEFAULT_BOTTOM_BAR_MARGIN_BOTTOM = 0;//dp
    private final int DEFAULT_PIC_DESC_TEXT_SIZE = 18;//sp
    private final int DEFAULT_PIC_DESC_TEXT_COLOR = Color.WHITE;
    private final int DEFAULT_BOTTOM_BAR_BG_COLOR = Color.parseColor("#77000000");

    private int mAutoTime = DEFAULT_AUTO_CAROUSEL_TIMEMS;
    private int mIndicatorSpace = DEFAULT_INDICATOR_SPACE_PADDING;
    private int mIndicatorMarginRight = DEFAULT_INDICATOR_MARGIN_RIGHT;
    private int mBottomBarHeight = DEFAULT_BOTTOM_BAR_HEIGHT;
    private int mPicDescMarginLeft = DEFAULT_PIC_DESC_MARGIN_LEFT;
    private int mBottomBarMarginBottom = DEFAULT_BOTTOM_BAR_MARGIN_BOTTOM;
    private int mPicDescTextSize = DEFAULT_PIC_DESC_TEXT_SIZE;
    private int mPicDescTextColor = DEFAULT_PIC_DESC_TEXT_COLOR;
    private int mBottomBarBGColor = DEFAULT_BOTTOM_BAR_BG_COLOR;


    private List<BannerInfo> mInfoList;
    private ViewPager.OnPageChangeListener mCarouselPagerListener;
    private OnCarouselClickListener mCarouselClickListener;
    private PicInitListener mPicInitListener;
    private int mCurrentIndex;
    private CarouselTask mCarouselTask;
    private CarouselAdapter mCarouselAdapter;
    private ViewPager mCarouselViewPager;
    private LinearLayout mIndicatorContainer;
    private RelativeLayout mBottombarContainer;
    private TextView mPicDescTextView;
    private boolean isDefaultBanner = false;


    private Context mContext;


    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.CarouselView);
        try {
            mAutoTime = a.getInt(R.styleable.CarouselView_auto_carousel_timeMS, DEFAULT_AUTO_CAROUSEL_TIMEMS);
            mIndicatorSpace = a.getDimensionPixelSize(R.styleable.CarouselView_indicator_space, dp2px(DEFAULT_INDICATOR_SPACE_PADDING));
            mIndicatorMarginRight = a.getDimensionPixelSize(R.styleable.CarouselView_indicator_margin_right, dp2px(DEFAULT_INDICATOR_MARGIN_RIGHT));
            mBottomBarHeight = a.getDimensionPixelSize(R.styleable.CarouselView_bottom_bar_height, dp2px(DEFAULT_BOTTOM_BAR_HEIGHT));
            mPicDescMarginLeft = a.getDimensionPixelSize(R.styleable.CarouselView_pic_desc_margin_left, dp2px(DEFAULT_PIC_DESC_MARGIN_LEFT));
            mBottomBarMarginBottom = a.getDimensionPixelSize(R.styleable.CarouselView_bottom_bar_margin_bottom, dp2px(DEFAULT_BOTTOM_BAR_MARGIN_BOTTOM));
            mPicDescTextSize = a.getDimensionPixelSize(R.styleable.CarouselView_pic_desc_text_size, sp2px(DEFAULT_PIC_DESC_TEXT_SIZE));
            mPicDescTextColor = a.getColor(R.styleable.CarouselView_pic_desc_text_color, DEFAULT_PIC_DESC_TEXT_COLOR);
            mBottomBarBGColor = a.getColor(R.styleable.CarouselView_bottom_bar_background_color, DEFAULT_BOTTOM_BAR_BG_COLOR);
            mPicDescTextSize = px2sp(mPicDescTextSize);
        } finally {
            a.recycle();
        }
        initUI();
    }

    private void initUI() {
        //添加viewpager
        mCarouselViewPager = new ViewPager(mContext);
        mCarouselViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mCarouselViewPager.setPageTransformer(true, new DepthPageTransformer());
//        mCarouselViewPager.setPageTransformer(true, new RotateDownPageTransformer());
        LayoutParams vpLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        mCarouselViewPager.setLayoutParams(vpLp);

        LayoutParams rlLp = new LayoutParams(LayoutParams.MATCH_PARENT
                , mBottomBarHeight);
        rlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //添加Bottombar
        mBottombarContainer = new RelativeLayout(mContext);
        mBottombarContainer.setBackgroundColor(mBottomBarBGColor);
        rlLp.setMargins(0, 0, 0, mBottomBarMarginBottom);
        mBottombarContainer.setLayoutParams(rlLp);


        LayoutParams indicatorLp = new LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.MATCH_PARENT);


        LayoutParams textViewLP = new LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.MATCH_PARENT);


        textViewLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewLP.setMargins(mPicDescMarginLeft, 0, 0, 0);

        indicatorLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        indicatorLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        indicatorLp.setMargins(0, 0, mIndicatorMarginRight, 0);


        //添加indicator容器
        mIndicatorContainer = new LinearLayout(mContext);
        mIndicatorContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        mBottombarContainer.addView(mIndicatorContainer, indicatorLp);


        //图片描述
        mPicDescTextView = new TextView(mContext);
        mPicDescTextView.setTextSize(mPicDescTextSize);
        mPicDescTextView.setTextColor(mPicDescTextColor);
        mPicDescTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mPicDescTextView.setPadding(5, 5, 5, 5);
        mBottombarContainer.addView(mPicDescTextView, textViewLP);

        this.addView(mCarouselViewPager);
        this.addView(mBottombarContainer);
    }

    public void setCarouselData(List<BannerInfo> carouselData) {
        if (carouselData == null || carouselData.size() == 0) {
            isDefaultBanner = true;
        } else {
            this.mInfoList = carouselData;
            isDefaultBanner = false;
        }
        //初始化轮播任务
        initCarouselTask();
        //初始化ViewPager
        initViewpager();
        //初始化指示器
        initIndicator();
        //初始化轮播图的显示
        setCarouselDesAndIndicator(mCurrentIndex);
        //跳转到第一个
        mCarouselViewPager.setCurrentItem(mCurrentIndex);
        //开始轮播任务
//        mCarouselTask.start();
        int count = mCarouselAdapter.getCount();
        Log.i("hate", "轮播图数量:" + count);
    }

    private void initViewpager() {
        mCurrentIndex = 0;
        if (!isDefaultBanner) {
            mCarouselTask.stop();//停止任务
            mCurrentIndex = 300 - (300 % mInfoList.size());
        }
        Log.i("hate", "当前位置:" + mCurrentIndex);
        mCarouselAdapter = new CarouselAdapter();
        mCarouselViewPager.setAdapter(mCarouselAdapter);
        mCarouselAdapter.notifyDataSetChanged();


        if (mCarouselPagerListener == null) {
            mCarouselPagerListener = new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    mCurrentIndex = position;
                    setCarouselDesAndIndicator(mCurrentIndex);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
            //给轮播图添加页面切换事件
            mCarouselViewPager.addOnPageChangeListener(mCarouselPagerListener);
        }
    }

    private void initIndicator() {
        //清空以前存在的点
        mIndicatorContainer.removeAllViews();
        //轮播图有几张 加几个点
        if (isDefaultBanner) {
            View v_point = new View(mContext);
            //设置点的背景选择器
            v_point.setBackgroundResource(R.drawable.carousel_indicator_seletor);
            v_point.setEnabled(false);//默认是默认的灰色点

            //设置点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(10), dp2px(10));
            //设置点与点直接的间距
            params.leftMargin = mIndicatorSpace;

            //设置参数
            v_point.setLayoutParams(params);
            mIndicatorContainer.addView(v_point);
        } else {
            for (int i = 0; i < mInfoList.size(); i++) {
                View v_point = new View(mContext);
                //设置点的背景选择器
                v_point.setBackgroundResource(R.drawable.carousel_indicator_seletor);
                v_point.setEnabled(false);//默认是默认的灰色点

                //设置点的大小
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(10), dp2px(10));
                //设置点与点直接的间距
                params.leftMargin = mIndicatorSpace;

                //设置参数
                v_point.setLayoutParams(params);
                mIndicatorContainer.addView(v_point);
            }
        }
    }

    private void initCarouselTask() {
        if (mCarouselTask == null)//初始化自动轮播任务
            mCarouselTask = new CarouselTask();
    }


    private void setCarouselDesAndIndicator(int position) {
        position %= mInfoList.size();
        Log.i("hate", "当前位置set:" + position);
        if (isDefaultBanner) {
            mIndicatorContainer.getChildAt(0).setEnabled(true);
        } else {
            if (position < 0)//||
                return;
            //设置描述信息
            mPicDescTextView.setText(mInfoList.get(position).name);
            //设置点是否是选中
            for (int i = 0; i < mInfoList.size(); i++) {
                mIndicatorContainer.getChildAt(i).setEnabled(i == position);
            }
        }
    }

    public void stop() {
        if (mCarouselTask != null)
            mCarouselTask.stop();
    }

    public void start(){
        if (mCarouselTask != null)
            mCarouselTask.start();
    }


    private class CarouselTask extends Handler implements Runnable {

        public void stop() {
            //移除当前所有的任务
            removeCallbacksAndMessages(null);
        }

        public void start() {
            if (isDefaultBanner)
                return;
            stop();
            if (mInfoList.size() <= 1)//只有一张不启动自动轮播任务
                return;
            postDelayed(this, mAutoTime);
        }

        @Override
        public void run() {
            //控制轮播图的显示
            mCarouselViewPager.setCurrentItem((mCarouselViewPager.getCurrentItem() + 1)
                    % mCarouselViewPager.getAdapter().getCount());
            postDelayed(this, mAutoTime);
        }
    }


    private class CarouselAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView ivCarouselPic = new ImageView(mContext);
            ivCarouselPic.setScaleType(ImageView.ScaleType.FIT_XY);

            //设备默认的图片,网络缓慢
//            ivCarouselPic.setImageResource(R.drawable.home_scroll_default);

            //异步加载图片，并且显示到组件中
            if (mPicInitListener != null) {
                if (isDefaultBanner) {
//                    Glide.with(mContext).load(R.drawable.default_banner).crossFade().into(ivCarouselPic);
                } else {
                    mPicInitListener.onPicInit(ivCarouselPic, mInfoList.get(position % mInfoList.size()).img, position % mInfoList.size());
                }
            }
//            Glide.with(mContext).load(sBannerList.get(position).img).crossFade().into(ivCarouselPic);

            //给图片添加触摸事件
            ivCarouselPic.setOnTouchListener(new OnTouchListener() {

                private float downX;
                private float downY;
                private long downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://按下停止轮播
                            downX = event.getX();
                            downY = event.getY();
                            downTime = System.currentTimeMillis();
                            mCarouselTask.stop();
                            break;
                        case MotionEvent.ACTION_CANCEL://事件取消
                            mCarouselTask.start();
                            break;
                        case MotionEvent.ACTION_UP://松开
                            float upX = event.getX();
                            float upY = event.getY();

                            if (upX == downX && upY == downY) {
                                long upTime = System.currentTimeMillis();
                                if (upTime - downTime < 500) {
                                    //点击
                                    if (mCarouselClickListener != null) {
                                        if (!isDefaultBanner)
                                            mCarouselClickListener.onCarouselClick(mInfoList.get(position % mInfoList.size()), position % mInfoList.size());
                                    }
                                }
                            }
                            mCarouselTask.start();//开始轮播
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            container.addView(ivCarouselPic);
            return ivCarouselPic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (isDefaultBanner || mInfoList.size() == 1)
                return 1;
            return Integer.MAX_VALUE;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    public ViewPager getCarouselViewPager() {
        return mCarouselViewPager;
    }

    public PagerAdapter getCarouselAdapter() {
        return mCarouselAdapter;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void reset(List<BannerInfo> infoList) {
        setCarouselData(infoList);
    }


    public void setPicInitListener(PicInitListener listener) {
        this.mPicInitListener = listener;
    }

    public void setOnCarouselClickListener(OnCarouselClickListener carouselClickListener) {
        this.mCarouselClickListener = carouselClickListener;
    }


    public interface PicInitListener {
        void onPicInit(ImageView imageView, String url, int position);
    }

    public interface OnCarouselClickListener {
        void onCarouselClick(BannerInfo bannerInfo, int position);
    }


    public static class BannerInfo {
        public String img;//图片地址
        public String url;//跳转地址
        public String name;//名称


        @Override
        public String toString() {
            return "BannerListBean{" +
                    "img='" + img + '\'' +
                    ", url='" + url + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    private int dp2px(float dpVal) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);

//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                dpVal, mContext.getResources().getDisplayMetrics());
    }


    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    private int sp2px(float spVal) {
//        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
//        return (int) (spVal * fontScale + 0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, mContext.getResources().getDisplayMetrics());
    }

    private int px2dp(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int px2sp(float pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
