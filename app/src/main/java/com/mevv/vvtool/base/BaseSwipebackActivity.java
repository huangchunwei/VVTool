package com.mevv.vvtool.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.mevv.vvtool.R;
import com.mevv.vvtool.util.ActManager;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by VV on 2016/10/22.
 */

public abstract class BaseSwipebackActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActManager.getInstances().add(this);
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL,EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
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
}

