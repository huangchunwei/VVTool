package com.mevv.vvtool.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mevv.vvtool.R;
import com.mevv.vvtool.util.ActManager;

/**
 * Created by VV on 2016/10/21.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
//    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActManager.getInstances().add(this);
        // API 19开始，安卓支持沉浸式项栏
       /* if (android.os.Build.VERSION.SDK_INT >= 19) {
            // 为适配沉浸式顶栏,在写XML布局时,在顶部的控件加上这两个属生
            // android:fitsSystemWindows="true"
            // android:clipToPadding="true"
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏不要打开,用虚拟导航按钮的手机会有问题
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        //不能竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.mContext = this;
        getIntentData();
        setContentView(bindLayout());
//        mUnbinder = ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        init();
    }

    public abstract int bindLayout();

    public abstract void init();

    public void getIntentData() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO 自定义界面关闭动画
        overridePendingTransition(0, R.anim.b_exit_anim);
    }

    /**
     * 设置全屏
     */
    public void setFullScreen() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     */
    public void quitFullScreen() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mUnbinder != null)
//            mUnbinder.unbind();
//        EventBus.getDefault().unregister(this);
    }
}
