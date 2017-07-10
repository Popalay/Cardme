package com.popalay.cardme.presentation.screens.cards;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxrelay2.PublishRelay;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.databinding.FragmentCardsBinding;
import com.popalay.cardme.presentation.base.BaseFragment;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity;
import com.popalay.cardme.presentation.widget.OnOneOffClickListener;
import com.popalay.cardme.utils.ShareUtils;
import com.popalay.cardme.utils.recycler.SimpleItemTouchHelperCallback;
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CardsFragment extends BaseFragment implements CardsView,
        SimpleItemTouchHelperCallback.SwipeCallback, ItemClickListener<Card>,
        SimpleItemTouchHelperCallback.DragCallback {

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
                final CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                presenter.onCardScanned(scanResult);
            }
        }
    }

    @Override
    public void startCardScanning() {
        final Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    @Override
    public void showRemoveUndoAction(Card card) {
        Snackbar.make(b.listCards, R.string.card_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> presenter.onRemoveUndo(card))
                .show();
    }

    @Override
    public void setViewModel(CardsViewModel viewModel) {
        b.setWm(viewModel);
    }

    @Override
    public void onSwiped(int position) {
        final Card card = b.getWm().get(position);
        presenter.onItemSwiped(card);
    }

    @Override
    public void onDragged(int from, int to) {
        presenter.onItemDragged(b.getWm().cards, from, to);
    }

    @Override
    public void onDropped() {
        presenter.onItemDropped(b.getWm().cards);
    }

    @Override
    public void onItemClick(@NonNull Card item) {
        presenter.onCardClick(item);
    }

    @Override
    public void shareCardNumber(String cardNumber) {
        ShareUtils.shareText(getActivity(), R.string.share_card, cardNumber);
    }

    private void initUI() {
        final PublishRelay<Card> publisher = PublishRelay.create();
        publisher.subscribe(presenter::onCardClick);
        b.setPublisher(publisher);

        b.listCards.addItemDecoration(new SpacingItemDecoration.Builder(getContext())
                .firstDivider(true)
                .lastDivider(true)
                .betweenItems(true)
                .onSides(true)
                .build());
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this, this))
                .attachToRecyclerView(b.listCards);
        b.buttonAdd.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                presenter.onAddClick();
            }
        });
    }
}
