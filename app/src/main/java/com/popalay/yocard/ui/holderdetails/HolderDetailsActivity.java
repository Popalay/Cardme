package com.popalay.yocard.ui.holderdetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.databinding.ActivityHolderDetailsBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.base.ItemClickListener;

import java.util.List;

public class HolderDetailsActivity extends BaseActivity implements HolderDetailsView, ItemClickListener<Card> {

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
        //setTitle("title");
        //b.collapsingToolbar.setTitle(name);
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

    private void initUI() {
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Tittle");
        b.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new HolderDetailsViewModel();
        b.setModel(viewModel);
        b.setListener(this);
    }
}
