package com.popalay.yocard.ui.cards;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.yocard.BR;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.databinding.ItemCardBinding;
import com.popalay.yocard.utils.DiffUtilCalback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardAdapterWrapper {

    private final LastAdapter adapter;
    private List<Card> items;

    public CardAdapterWrapper(CardsView.CardListener listener) {
        this.items = new ArrayList<>();
        this.adapter = LastAdapter.with(items, BR.item, true)
                .map(Card.class, new ItemType<ItemCardBinding>(R.layout.item_card) {
                    @Override
                    public void onBind(@NotNull ItemCardBinding binding, @NotNull View view, int position) {
                        super.onBind(binding, view, position);
                        binding.setListener(listener);
                    }
                });
    }

    public void attachToRecycler(RecyclerView recyclerView) {
        this.adapter.into(recyclerView);
    }

    public void setItems(List<Card> newItems) {
        final List<Card> oldItems = new ArrayList<>(this.items);
        this.items.clear();
        this.items.addAll(newItems);
        if (oldItems.isEmpty()) {
            adapter.notifyDataSetChanged();
        } else {
            DiffUtil.calculateDiff(new DiffUtilCalback(oldItems, newItems), true)
                    .dispatchUpdatesTo(adapter);
        }
    }

    public Card getItem(int position) {
        return this.items.get(position);
    }

    public void clear() {
        this.items.clear();
        this.adapter.notifyDataSetChanged();
    }
}
