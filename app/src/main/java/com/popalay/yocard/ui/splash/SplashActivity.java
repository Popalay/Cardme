package com.popalay.yocard.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            startActivity(HomeActivity.getIntent(this));
            finish();
        }, 1000);
    }
}
