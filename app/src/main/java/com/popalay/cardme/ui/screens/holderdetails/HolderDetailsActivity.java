package com.popalay.cardme.ui.screens.holderdetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding;
import com.popalay.cardme.ui.base.BaseActivity;
import com.popalay.cardme.ui.base.ItemClickListener;
import com.popalay.cardme.utils.ShareUtils;

import java.util.List;

public class HolderDetailsActivity extends BaseActivity implements HolderDetailsView, ItemClickListener<Card> {

    private static final String TAG = "HolderDetailsActivity";

    private static final String KEY_HOLDER_ID = "HOLDER_ID";

    @InjectPresenter HolderDetailsPresenter presenter;

    private ActivityHolderDetailsBinding b;

    private HolderDetailsViewModel viewModel;

    public static Intent getIntent(Context context, Holder holder) {
        final Intent intent = new Intent(context, HolderDetailsActivity.class);
        intent.putExtra(KEY_HOLDER_ID, holder.getId());
        return intent;
    }

    @ProvidePresenter
    HolderDetailsPresenter providePresenter() {
        return new HolderDetailsPresenter(getIntent().getLongExtra(KEY_HOLDER_ID, -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_holder_details);
        initUI();
    }

    @Override
    public void setHolderName(String name) {
        setTitle(name);
    }

    @Override
    public void setCards(List<Card> cards) {
        viewModel.setCards(cards);
    }

    @Override
    public void setDebts(List<Debt> debts) {
        viewModel.setDebts(debts);
    }

    @Override
    public void onItemClick(Card item) {
        presenter.onCardClick(item);
    }

    @Override
    public void shareCardNumber(String cardNumber) {
        ShareUtils.shareText(this, R.string.share_card, cardNumber);
    }

    private void initUI() {
        viewModel = new HolderDetailsViewModel();
        b.setModel(viewModel);
        b.setListener(this);

        setSupportActionBar(b.toolbar);
        b.collapsingToolbar.setTitleEnabled(false);
        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (appBarLayout.getTotalScrollRange() == 0) {
                return;
            }
            final int alpha = Math.min(-verticalOffset, 255);
            b.toolbar.setTitleTextColor(Color.argb(alpha, 255, 255, 255));
        });
    }
}
