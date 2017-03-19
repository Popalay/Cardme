package com.popalay.yocard.ui.addcard;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.databinding.ActivityAddCardBinding;
import com.popalay.yocard.ui.addcard.models.AddCardViewModel;
import com.popalay.yocard.ui.base.BaseActivity;

import java.util.List;

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
        b.setModel(new AddCardViewModel(card));
    }

    @Override
    public void setCompletedCardHolders(List<String> holders) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, holders);
        b.textHolder.setAdapter(adapter);
    }

    private void initUI() {
        setActionBar(b.toolbar);
        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.buttonSave.setOnClickListener(v -> presenter.onAcceptClick(b.getModel().getCard()));
    }
}
