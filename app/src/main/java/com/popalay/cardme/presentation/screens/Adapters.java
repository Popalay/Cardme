package com.popalay.cardme.presentation.screens;

import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.ItemCardBinding;
import com.popalay.cardme.databinding.ItemHolderBinding;
import com.popalay.cardme.presentation.base.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapters {

    private Adapters() {}

    @BindingAdapter(value = {"cardsAdapter", "cardClick", "showImage"}, requireAll = false)
    public static void cardsAdapter(RecyclerView recyclerView, List<Card> items,
            ItemClickListener listener, ObservableBoolean showImage) {
        if (recyclerView.getAdapter() != null || items == null || items.isEmpty()) return;
        new LastAdapter(items, BR.item, true)
                .map(Card.class, new ItemType<ItemCardBinding>(R.layout.item_card) {
                    @Override
                    public void onCreate(@NotNull com.github.nitrico.lastadapter.Holder<ItemCardBinding> holder) {
                        super.onCreate(holder);
                        holder.getBinding().setListener(listener);
                        holder.getBinding().cardView.setWithImage(showImage.get());
                        showImage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable observable, int i) {
                                holder.getBinding().cardView.setWithImage(showImage.get());
                            }
                        });
                    }
                }).into(recyclerView);

    }

    @BindingAdapter("debtsAdapter")
    public static void debtsAdapter(RecyclerView recyclerView, List<Debt> items) {
        if (recyclerView.getAdapter() != null || items == null || items.isEmpty()) return;
        new LastAdapter(items, BR.item, true)
                .map(Debt.class, R.layout.item_debt)
                .into(recyclerView);
    }

    @BindingAdapter(value = {"holdersAdapter", "holderClick"}, requireAll = false)
    public static void holdersAdapter(RecyclerView recyclerView, List<Holder> items,
            ItemClickListener listener) {
        if (recyclerView.getAdapter() != null || items == null || items.isEmpty()) return;
        new LastAdapter(items, BR.item, true)
                .map(Holder.class, new ItemType<ItemHolderBinding>(R.layout.item_holder) {
                    @Override
                    public void onCreate(@NotNull com.github.nitrico.lastadapter.Holder<ItemHolderBinding> holder) {
                        super.onCreate(holder);
                        holder.getBinding().setListener(listener);
                    }
                }).into(recyclerView);
    }
}
