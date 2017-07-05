package com.popalay.cardme.presentation.screens.holderdetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.popalay.cardme.App;
import com.popalay.cardme.R;
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding;
import com.popalay.cardme.presentation.base.CustomFactory;
import com.popalay.cardme.presentation.base.SlidingActivity;
import com.popalay.cardme.utils.ShareUtils;
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration;

import javax.inject.Inject;

public class HolderDetailsActivity extends SlidingActivity {

    @Inject CustomFactory factory;

    private ActivityHolderDetailsBinding b;
    private boolean isExpanded;

    public static Intent getIntent(Context context) {
        return new Intent(context, HolderDetailsActivity.class);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.appComponent().inject(this);
        b = DataBindingUtil.setContentView(this, R.layout.activity_holder_details);
        final HolderDetailsViewModel vm = ViewModelProviders.of(this, factory).get(HolderDetailsViewModel.class);
        b.setVm(vm);
        initUI();
    }

    @NonNull @Override protected View getRootView() {
        return b.getRoot();
    }

    @Override protected boolean canSlideDown() {
        return isExpanded && b.listDebts.getScrollY() == 0;
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        b.collapsingToolbar.setTitleEnabled(false);
        b.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        b.getVm().doOnShareCard().subscribe(number -> ShareUtils.shareText(this, R.string.share_card, number));

        b.listCards.addItemDecoration(new SpacingItemDecoration.Builder(this)
                .onSides(true)
                .betweenItems(true)
                .build());

        assert b.toolbar.getNavigationIcon() != null;
        b.toolbar.getNavigationIcon().setAlpha(255);
        b.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            isExpanded = verticalOffset == 0;
            if (appBarLayout.getTotalScrollRange() == 0) {
                return;
            }
            final int alpha = Math.min(-verticalOffset, 255);
            b.toolbar.setTitleTextColor(Color.argb(alpha, 255, 255, 255));
            b.toolbar.getNavigationIcon().setAlpha(alpha);
        });

    }
}
