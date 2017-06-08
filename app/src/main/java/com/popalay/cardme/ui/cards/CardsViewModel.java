package com.popalay.cardme.ui.cards;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.databinding.ItemCardBinding;
import com.popalay.cardme.ui.base.ItemClickListener;
import com.popalay.cardme.utils.recycler.DiffUtilCallback;
import com.popalay.cardme.utils.recycler.SpacingItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardsViewModel {

    public final ObservableField<List<Card>> cards = new ObservableField<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    @BindingAdapter(value = {"cards", "itemClickListener", "showImage"}, requireAll = false)
    public static void setCards(RecyclerView recyclerView, List<Card> newItems,
            ItemClickListener listener, Boolean showImage) {
        if (newItems == null) {
            return;
        }
        final List<Card> items;
        final Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() == null) {
            recyclerView.addItemDecoration(new SpacingItemDecoration(context, true, true, true, true));
            items = new ArrayList<>(newItems);
            new LastAdapter(items, BR.item, true)
                    .map(Card.class, new ItemType<ItemCardBinding>(R.layout.item_card) {
                        @Override public void onCreate(@NotNull Holder<ItemCardBinding> holder) {
                            super.onCreate(holder);
                            holder.getBinding().setListener(listener);
                        }

                        @Override public void onBind(Holder<ItemCardBinding> holder) {
                            super.onBind(holder);
                            holder.getBinding().cardView.setWithImage(showImage);
                        }
                    })
                    .into(recyclerView);
            recyclerView.setTag(R.id.recycler_data, items);
        } else {
            // FIXME: 09.06.17 UPDATE SHOW IMAGE FIELD
            //noinspection unchecked
            items = ((List<Card>) recyclerView.getTag(R.id.recycler_data));
            DiffUtil.calculateDiff(new DiffUtilCallback(items, newItems), true)
                    .dispatchUpdatesTo(recyclerView.getAdapter());
            items.clear();
            items.addAll(newItems);
        }
    }

    public void setShowImage(Boolean show) {
        showImage.set(show);
        cards.notifyChange();
    }

    public void setCards(List<Card> items) {
        cards.set(items);
    }

    public Card get(int position) {
        return cards.get().get(position);
    }

}
