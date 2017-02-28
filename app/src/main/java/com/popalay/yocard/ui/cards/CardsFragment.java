package com.popalay.yocard.ui.cards;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.databinding.FragmentCardsBinding;
import com.popalay.yocard.ui.addcard.AddCardActivity;
import com.popalay.yocard.ui.base.BaseFragment;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CardsFragment extends BaseFragment implements CardsView {

    private static final String TAG = "CardsFragment";
    private static final int SCAN_REQUEST_CODE = 121;

    @InjectPresenter CardsPresenter presenter;

    private FragmentCardsBinding b;

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false);
        initUI();
        return b.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                presenter.onCardScanned(scanResult);
            }
        }
    }

    @Override
    public void startCardScanning() {
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    @Override
    public void addCardDetails(CreditCard card) {
        startActivity(AddCardActivity.getIntent(getActivity(), card));
    }

    private void initUI() {
        b.buttonAdd.setOnClickListener(v -> presenter.onAddButtonClick());
    }
}
