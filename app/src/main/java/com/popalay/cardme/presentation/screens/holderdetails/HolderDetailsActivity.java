package com.popalay.cardme.presentation.screens.holderdetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.PagerSnapHelper;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding;
import com.popalay.cardme.presentation.adapter.DebtsAdapter;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.presentation.base.SlidingActivity;
import com.popalay.cardme.utils.ShareUtils;
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration;

import rx.android.schedulers.AndroidSchedulers;

public class HolderDetailsActivity extends SlidingActivity implements HolderDetailsView, ItemClickListener<Card> {

    private static final String KEY_HOLDER_ID = "HOLDER_ID";

    @InjectPresenter HolderDetailsPresenter presenter;

    private ActivityHolderDetailsBinding b;
    private DebtsAdapter debtsAdapter;
    private boolean isExpanded;

    public static Intent getIntent(Context context, Holder holder) {
        final Intent intent = new Intent(context, HolderDetailsActivity.class);
        intent.putExtra(KEY_HOLDER_ID, holder.getId());
        return intent;
    }

    @ProvidePresenter HolderDetailsPresenter providePresenter() {
        return new HolderDetailsPresenter(getIntent().getLongExtra(KEY_HOLDER_ID, -1));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_holder_details);
        initUI();
    }

    @Override public void setViewModel(HolderDetailsViewModel viewModel) {
        b.setWm(viewModel);

        addSubscription(viewModel.debtsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(debtsAdapter::setItems));
    }

    @Override public void onItemClick(Card item) {
        presenter.onCardClick(item);
    }

    @Override public void shareCardNumber(String cardNumber) {
        ShareUtils.shareText(this, R.string.share_card, cardNumber);
    }

    @Override protected View getRootView() {
        return b.getRoot();
    }

    @Override protected boolean canSlideDown() {
        return isExpanded && b.listDebts.getScrollY() == 0;
    }

    private void initUI() {
        b.setListener(this);
        setSupportActionBar(b.toolbar);
        b.collapsingToolbar.setTitleEnabled(false);
        b.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        b.listCards.addItemDecoration(new SpacingItemDecoration.Builder(this)
                .onSides(true)
                .betweenItems(true)
                .build());
        new PagerSnapHelper().attachToRecyclerView(b.listCards);

        debtsAdapter = new DebtsAdapter();
        b.listDebts.setAdapter(debtsAdapter);

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
