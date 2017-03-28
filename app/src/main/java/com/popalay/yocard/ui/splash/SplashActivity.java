package com.popalay.yocard.ui.splash;

import android.os.Bundle;

import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(HomeActivity.getIntent(this));
        finish();
    }
}
