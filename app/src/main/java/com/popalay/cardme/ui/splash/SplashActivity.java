package com.popalay.cardme.ui.splash;

import android.os.Bundle;

import com.popalay.cardme.ui.base.BaseActivity;
import com.popalay.cardme.ui.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(HomeActivity.getIntent(this));
        finish();
    }
}
