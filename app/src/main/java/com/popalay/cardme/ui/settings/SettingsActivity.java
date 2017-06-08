package com.popalay.cardme.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.popalay.cardme.R;
import com.popalay.cardme.databinding.ActivitySettingsBinding;
import com.popalay.cardme.ui.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding b;

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initUI();
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b.toolbar.setNavigationOnClickListener(v -> finish());
    }
}
