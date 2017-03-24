package com.popalay.yocard.ui.holderdetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.SnapHelper;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.databinding.ActivityHolderDetailsBinding;
import com.popalay.yocard.ui.adapters.CardAdapterWrapper;
import com.popalay.yocard.ui.adapters.DebtAdapterWrapper;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.utils.recycler.DividerItemDecoration;
import com.popalay.yocard.utils.recycler.HorizontalDividerItemDecoration;

import java.util.List;

public class HolderDetailsActivity extends BaseActivity implements HolderDetailsView, CardAdapterWrapper.CardListener {

    private static final String KEY_HOLDER_ID = "HOLDER_ID";

    @InjectPresenter HolderDetailsPresenter presenter;

    private ActivityHolderDetailsBinding b;

    private CardAdapterWrapper cardsAdapterWrapper;
    private DebtAdapterWrapper debtsAdapterWrapper;

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
        b.toolbar.setTitle(name);
    }

    @Override
    public void setCards(List<Card> cards) {
        cardsAdapterWrapper.setItems(cards);
    }

    @Override
    public void setDebts(List<Debt> debts) {
        debtsAdapterWrapper.setItems(debts);
    }

    @Override
    public void onCardClick(Card card) {
        presenter.onCardClick(card);
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        setTitle(null);
        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.listCards.addItemDecoration(new DividerItemDecoration(this, true, true, true, true));
        b.listDebts.addItemDecoration(new HorizontalDividerItemDecoration(this, R.color.grey, 1,
                getResources().getDimensionPixelSize(R.dimen.title_offset), 0));

        cardsAdapterWrapper = new CardAdapterWrapper(this);
        cardsAdapterWrapper.attachToRecycler(b.listCards);

        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(b.listCards);

        debtsAdapterWrapper = new DebtAdapterWrapper();
        debtsAdapterWrapper.attachToRecycler(b.listDebts);
    }
}
