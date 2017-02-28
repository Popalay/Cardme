package com.popalay.yocard.ui.addcard;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.databinding.ActivityAddCardBinding;
import com.popalay.yocard.ui.base.BaseActivity;

import io.card.payment.CreditCard;

public class AddCardActivity extends BaseActivity implements AddCardView {

    private static final String EXTRA_CARD = "EXTRA_CARD";

    @InjectPresenter AddCardPresenter presenter;

    private ActivityAddCardBinding b;

    public static Intent getIntent(Context context, CreditCard card) {
        final Intent intent = new Intent(context, AddCardActivity.class);
        intent.putExtra(EXTRA_CARD, card);
        return intent;
    }

    @ProvidePresenter
    AddCardPresenter providePresenter() {
        return new AddCardPresenter(getIntent().getParcelableExtra(EXTRA_CARD));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_add_card);
        initUI();
    }

    @Override
    public void showCardDetails(Card card) {
        b.setCard(card);
    }

    private void initUI() {
        setActionBar(b.toolbar);
        b.toolbar.setNavigationOnClickListener(v -> finish());
        b.buttonAccept.setOnClickListener(v -> presenter.onAccept(b.getCard()));
    }
}
