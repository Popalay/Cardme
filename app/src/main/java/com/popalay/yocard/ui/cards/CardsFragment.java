package com.popalay.yocard.ui.cards;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.popalay.yocard.ui.base.ItemClickListener;
import com.popalay.yocard.utils.recycler.SimpleItemTouchHelperCallback;

import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CardsFragment extends BaseFragment implements CardsView,
        SimpleItemTouchHelperCallback.SwipeCallback, ItemClickListener<Card> {

    private static final int SCAN_REQUEST_CODE = 121;

    @InjectPresenter CardsPresenter presenter;

    private FragmentCardsBinding b;
    private CardsViewModel viewModel;

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false);
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
        final Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    @Override
    public void addCardDetails(CreditCard card) {
        startActivity(AddCardActivity.getIntent(getActivity(), card));
    }

    @Override
    public void showRemoveUndoAction(Card card) {
        Snackbar.make(b.listCards, R.string.card_removed, Snackbar.LENGTH_SHORT)
                .setAction(R.string.action_undo, view -> presenter.onRemoveUndo(card))
                .show();
    }

    @Override
    public void setItems(List<Card> items) {
        viewModel.setCards(items);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        final Card card = viewModel.get(viewHolder.getAdapterPosition());
        presenter.onItemSwiped(card);
    }

    @Override
    public void onItemClick(Card item) {
        presenter.onCardClick(item);
    }

    private void initUI() {
        viewModel = new CardsViewModel();
        b.setModel(viewModel);
        b.setListener(this);

        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this)).attachToRecyclerView(b.listCards);
        b.buttonAdd.setOnClickListener(v -> presenter.onAddClick());
    }
}
