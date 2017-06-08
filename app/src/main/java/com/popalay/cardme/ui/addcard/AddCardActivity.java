package com.popalay.cardme.ui.addcard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.cardme.R;
import com.popalay.cardme.databinding.ActivityAddCardBinding;
import com.popalay.cardme.ui.base.BaseActivity;
import com.popalay.cardme.utils.DialogFactory;

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
    public void showCardDetails(AddCardViewModel vm) {
        b.setModel(vm);
    }

    @Override
    public void setCompletedCardHolders(List<String> holders) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, holders);
        b.textHolder.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        final Dialog errorDialog = DialogFactory.createCustomButtonsDialog(this, message,
                getString(R.string.action_close), null,
                (dialog, which) -> presenter.onCloseClick(), dialog -> presenter.onErrorDialogDismiss());

        errorDialog.setCancelable(false);
        errorDialog.show();
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.buttonSave.setOnClickListener(v -> presenter.onAcceptClick(b.getModel().card));

        b.textTitle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (b.buttonSave.isEnabled()) {
                    b.buttonSave.performClick();
                }
                return true;
            }
            return false;
        });

    }
}
