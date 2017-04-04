package com.popalay.yocard.ui.cards;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.yocard.BR;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.databinding.ItemCardBinding;
import com.popalay.yocard.ui.base.ItemClickListener;
import com.popalay.yocard.utils.recycler.DiffUtilCallback;
import com.popalay.yocard.utils.recycler.DividerItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardsViewModel {

    public final ObservableField<List<Card>> cards = new ObservableField<>();

    @BindingAdapter(value = {"bind:cards", "bind:itemClickListener"}, requireAll = false)
    public static void setCards(RecyclerView recyclerView, List<Card> newItems, ItemClickListener listener) {
        if (newItems == null) {
            return;
        }
        final List<Card> items;
        final Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() == null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(context, true, true, true, true));
            items = new ArrayList<>(newItems);
            LastAdapter.with(items, BR.item, true)
                    .map(Card.class, new ItemType<ItemCardBinding>(R.layout.item_card) {
                        @Override
                        public void onBind(@NotNull ItemCardBinding binding, @NotNull View view, int position) {
                            super.onBind(binding, view, position);
                            binding.setListener(listener);
                        }
                    })
                    .into(recyclerView);
            recyclerView.setTag(R.id.recycler_data, items);
        } else {
            //noinspection unchecked
            items = ((List<Card>) recyclerView.getTag(R.id.recycler_data));
            DiffUtil.calculateDiff(new DiffUtilCallback(items, newItems), true)
                    .dispatchUpdatesTo(recyclerView.getAdapter());
            items.clear();
            items.addAll(newItems);
        }
    }

    public void setCards(List<Card> items) {
        cards.set(items);
        cards.notifyChange();
    }

    public Card get(int position) {
        return cards.get().get(position);
    }

}
