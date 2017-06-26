package com.popalay.cardme.presentation.screens.cards;

import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.databinding.ItemCardBinding;
import com.popalay.cardme.presentation.base.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardsViewModel {

    public final ObservableList<Card> cards = new ObservableArrayList<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    public void setShowImage(boolean show) {
        showImage.set(show);
    }

    public void setCards(List<Card> items) {
        final List<Card> newList = new ArrayList<>(items);
        cards.clear();
        cards.addAll(newList);
    }

    public Card get(int position) {
        return cards.get(position);
    }

    @BindingAdapter(value = {"cardsAdapter", "itemClickListener", "showImage"}, requireAll = false)
    public static void cardsAdapter(RecyclerView recyclerView, List<Card> items,
            ItemClickListener listener, ObservableBoolean showImage) {
        if (recyclerView.getAdapter() != null || items == null) return;
        new LastAdapter(items, BR.item, true)
                .map(Card.class, new ItemType<ItemCardBinding>(R.layout.item_card) {
                    @Override public void onCreate(@NotNull Holder<ItemCardBinding> holder) {
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

}
