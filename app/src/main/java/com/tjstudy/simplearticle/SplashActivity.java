package com.tjstudy.simplearticle;

import android.os.Bundle;
import android.os.Handler;

import com.tjstudy.simplearticle.base.BaseActivity;
import com.tjstudy.simplearticle.ui.MainActivity;

/**
 * 闪屏 Splash界面，设置2s之后跳转到主界面
 */
public class SplashActivity extends BaseActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startActivity(SplashActivity.this);
                finish();//结束闪屏界面
            }
        }, 2000);
    }
}
