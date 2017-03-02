package com.popalay.yocard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.ActivityHomeBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.cards.CardsFragment;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding b;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initUI();
    }

    private void initUI() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.host, CardsFragment.newInstance())
                .commitAllowingStateLoss();
    }
}
