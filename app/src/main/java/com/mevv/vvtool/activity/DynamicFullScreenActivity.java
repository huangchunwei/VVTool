package com.mevv.vvtool.activity;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.mevv.vvtool.R;
import com.mevv.vvtool.base.BaseSwipebackActivity;

/**
 * Created by VV on 2016/10/22.
 */

public class DynamicFullScreenActivity extends BaseSwipebackActivity implements View.OnClickListener {

    private Button mSetFullScreen;
    private Button mChangeStatusBarAlph;
    private Button mChangeStatusBarColor;

    private int mClickCount;

    @Override
    public int bindLayout() {
        return R.layout.activity_dynamic_full_screen;
    }

    @Override
    public void init() {
        mSetFullScreen = (Button) findViewById(R.id.btn_set_full_screen);
        mChangeStatusBarAlph = (Button) findViewById(R.id.btn_change_status_bar_alph);
        mChangeStatusBarColor = (Button) findViewById(R.id.btn_change_status_bar_color);

        mSetFullScreen.setOnClickListener(this);
        mChangeStatusBarAlph.setOnClickListener(this);
        mChangeStatusBarColor.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_full_screen:
                if (mClickCount % 2 == 0) {
                    setFullScreen();
                    mClickCount++;
                    mSetFullScreen.setText("退出全屏");
                } else {
                    quitFullScreen();
                    mClickCount++;
                    mSetFullScreen.setText("进入全屏");
                }
                break;
            case R.id.btn_change_status_bar_alph:
                changeStatusBarAlph();
                break;
            case R.id.btn_change_status_bar_color:
                changeStatusBarColor();
                break;
        }
    }

    public void changeStatusBarAlph() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                // 透明状态栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏不要打开,用虚拟导航按钮的手机会有问题
//                 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
            //底部导航栏
            getWindow().setNavigationBarColor(this.getResources().getColor(R.color.colorAccent));
        }
    }
}
