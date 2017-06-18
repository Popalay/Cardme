package com.popalay.cardme.presentation.screens.splash;

import android.os.Bundle;

import com.popalay.cardme.presentation.base.BaseActivity;
import com.popalay.cardme.presentation.screens.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(HomeActivity.getIntent(this));
        finish();
    }
}
