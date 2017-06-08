package com.popalay.cardme.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.cardme.R;
import com.popalay.cardme.databinding.ActivitySettingsBinding;
import com.popalay.cardme.ui.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingView {

    @InjectPresenter SettingPresenter presenter;

    private ActivitySettingsBinding b;

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initUI();
    }

    @Override public void setSettings(SettingsViewModel vm) {
        b.setVm(vm);
    }

    @Override protected void onDestroy() {
        presenter.onClose();
        super.onDestroy();
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b.toolbar.setNavigationOnClickListener(v -> finish());
    }
}
