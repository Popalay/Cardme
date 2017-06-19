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
import com.popalay.cardme.presentation.adapter.CardsAdapter;
import com.popalay.cardme.presentation.adapter.DebtsAdapter;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.presentation.base.SlidingActivity;
import com.popalay.cardme.utils.ShareUtils;
import com.popalay.cardme.utils.recycler.decoration.HorizontalDividerItemDecoration;
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration;

import rx.android.schedulers.AndroidSchedulers;

public class HolderDetailsActivity extends SlidingActivity implements HolderDetailsView, ItemClickListener<Card> {

    private static final String KEY_HOLDER_ID = "HOLDER_ID";

    @InjectPresenter HolderDetailsPresenter presenter;

    private ActivityHolderDetailsBinding b;
    private DebtsAdapter debtsAdapter;
    private CardsAdapter cardsAdapter;
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
        b.setModel(viewModel);

        addSubscription(viewModel.cardsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cardsAdapter::setItems));

        addSubscription(viewModel.debtsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(debtsAdapter::setItems));

        addSubscription(viewModel.showImageObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cardsAdapter::setShowBackgrounds));
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

    @Override protected void onSlidingFinished() {

    }

    @Override protected void onSlidingStarted() {

    }

    @Override protected boolean canSlideDown() {
        return isExpanded && b.listDebts.getScrollY() == 0;
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        b.collapsingToolbar.setTitleEnabled(false);
        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.listDebts.addItemDecoration(new HorizontalDividerItemDecoration(this, R.color.grey, 1,
                this.getResources().getDimensionPixelSize(R.dimen.title_offset), 0));
        b.listCards.addItemDecoration(new SpacingItemDecoration(this, true, true, true, true));
        new PagerSnapHelper().attachToRecyclerView(b.listCards);

        cardsAdapter = new CardsAdapter();
        cardsAdapter.setItemClickListener(this);
        b.listCards.setAdapter(cardsAdapter);

        debtsAdapter = new DebtsAdapter();
        b.listDebts.setAdapter(debtsAdapter);

        b.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            isExpanded = verticalOffset == 0;
            if (appBarLayout.getTotalScrollRange() == 0) {
                return;
            }
            final int alpha = Math.min(-verticalOffset, 255);
            b.toolbar.setTitleTextColor(Color.argb(alpha, 255, 255, 255));
        });
    }
}
