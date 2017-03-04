package com.popalay.yocard.ui.cards;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.databinding.FragmentCardsBinding;
import com.popalay.yocard.ui.addcard.AddCardActivity;
import com.popalay.yocard.ui.base.BaseFragment;
import com.popalay.yocard.utils.DividerItemDecoration;
import com.popalay.yocard.utils.SimpleItemTouchHelperCallback;

import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CardsFragment extends BaseFragment implements CardsView, CardsView.CardListener,
        SimpleItemTouchHelperCallback.SwipeCallback {

    private static final int SCAN_REQUEST_CODE = 121;

    @InjectPresenter CardsPresenter presenter;

    private FragmentCardsBinding b;
    private CardAdapterWrapper adapterWrapper;

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false);
        b.setPresenter(presenter);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
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

    @Override
    public void setCards(List<Card> cards) {
        adapterWrapper.setItems(cards);
    }

    @Override
    public void onCardClick(Card card) {
        presenter.onCardClick(card);
    }

    @Override
    public void removeCard(int position) {
        adapterWrapper.remove(position);
    }

    @Override
    public void addCard(Card card, int position) {
        adapterWrapper.add(card, position);
    }

    @Override
    public void showRemoveUndoAction(Card card, int position) {
        Snackbar.make(b.listCards, R.string.card_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> presenter.onRemoveUndo(card, position))
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event == DISMISS_EVENT_ACTION) {
                            return;
                        }
                        presenter.onRemoveUndoActionDismissed(card, position);
                    }
                })
                .show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final Card card = adapterWrapper.get(position);
        presenter.onCardSwiped(card, position);
    }

    private void initUI() {
        b.listCards.addItemDecoration(new DividerItemDecoration(getActivity(), true, true, true, true));
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this))
                .attachToRecyclerView(b.listCards);
        adapterWrapper = new CardAdapterWrapper(this);
        adapterWrapper.attachToRecycler(b.listCards);
    }
}
