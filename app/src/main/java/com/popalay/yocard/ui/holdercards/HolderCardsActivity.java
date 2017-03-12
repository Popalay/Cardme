package com.popalay.yocard.ui.holdercards;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.databinding.ActivityHolderCardsBinding;
import com.popalay.yocard.ui.adapters.CardAdapterWrapper;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.utils.recycler.DividerItemDecoration;
import com.popalay.yocard.utils.recycler.SimpleItemTouchHelperCallback;

import java.util.List;

public class HolderCardsActivity extends BaseActivity implements HolderCardsView,
        SimpleItemTouchHelperCallback.SwipeCallback, CardAdapterWrapper.CardListener {

    private static final String KEY_HOLDER_ID = "HOLDER_ID";

    @InjectPresenter HolderCardsPresenter presenter;

    private ActivityHolderCardsBinding b;
    private CardAdapterWrapper adapterWrapper;

    public static Intent getIntent(Context context, Holder holder) {
        final Intent intent = new Intent(context, HolderCardsActivity.class);
        intent.putExtra(KEY_HOLDER_ID, holder.getId());
        return intent;
    }

    @ProvidePresenter
    HolderCardsPresenter providePresenter() {
        return new HolderCardsPresenter(getIntent().getLongExtra(KEY_HOLDER_ID, -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_holder_cards);
        initUI();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final Card card = adapterWrapper.get(position);
        presenter.onCardSwiped(card, position);
    }

    @Override
    public void setHolderName(String name) {
        setTitle(name);
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
    public void resetCard(Card card, int position) {
        adapterWrapper.add(card, position);
        b.listCards.smoothScrollToPosition(position);
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

    private void initUI() {
        setActionBar(b.toolbar);
        setTitle(null);
        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.listCards.addItemDecoration(new DividerItemDecoration(this, true, true, true, true));
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this)).attachToRecyclerView(b.listCards);
        adapterWrapper = new CardAdapterWrapper(this);
        adapterWrapper.attachToRecycler(b.listCards);
    }
}
