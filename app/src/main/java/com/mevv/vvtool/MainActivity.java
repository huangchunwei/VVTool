package com.mevv.vvtool;

import android.view.KeyEvent;
import android.view.View;

import com.mevv.vvtool.activity.CarouselViewActivity;
import com.mevv.vvtool.activity.DynamicFullScreenActivity;
import com.mevv.vvtool.base.BaseActivity;
import com.mevv.vvtool.util.ActManager;
import com.mevv.vvtool.util.SceneManager;
import com.mevv.vvtool.util.TipUtil;

public class MainActivity extends BaseActivity {

    private long mTime;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {

    }

    public void go2CarouselView(View v) {
        SceneManager.toScene(this, CarouselViewActivity.class, null);
    }

    public void go2FullScreen(View v){
        SceneManager.toScene(this, DynamicFullScreenActivity.class, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mTime > 2000) {
                TipUtil.showToast(this, "再按一次退出");
                mTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActManager.getInstances().finishAll();
    }
}
