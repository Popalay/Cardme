package com.popalay.cardme.ui.holderdetails;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.databinding.ItemCardBinding;
import com.popalay.cardme.ui.base.ItemClickListener;
import com.popalay.cardme.utils.recycler.DiffUtilCallback;
import com.popalay.cardme.utils.recycler.HorizontalDividerItemDecoration;
import com.popalay.cardme.utils.recycler.SpacingItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HolderDetailsViewModel {

    public final ObservableField<List<Debt>> debts = new ObservableField<>();
    public final ObservableField<List<Card>> cards = new ObservableField<>();

    @BindingAdapter("bind:holderDebts")
    public static void setDebts(RecyclerView recyclerView, List<Debt> newItems) {
        if (newItems == null) {
            return;
        }
        final List<Debt> items;
        final Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() == null) {
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(context, R.color.grey, 1,
                    context.getResources().getDimensionPixelSize(R.dimen.title_offset), 0));
            items = new ArrayList<>(newItems);
            new LastAdapter(items, BR.item, true)
                    .map(Debt.class, R.layout.item_debt)
                    .into(recyclerView);
            recyclerView.setTag(R.id.recycler_data, items);
        } else {
            //noinspection unchecked
            items = ((List<Debt>) recyclerView.getTag(R.id.recycler_data));
            DiffUtil.calculateDiff(new DiffUtilCallback(items, newItems), true)
                    .dispatchUpdatesTo(recyclerView.getAdapter());
            items.clear();
            items.addAll(newItems);
        }
    }

    @BindingAdapter(value = {"bind:holderCards", "bind:itemClickListener"}, requireAll = false)
    public static void setCards(RecyclerViewPager recyclerView, List<Card> newItems, ItemClickListener listener) {
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
                        @Override
                        public void onCreate(@NotNull Holder<ItemCardBinding> holder) {
                            super.onCreate(holder);
                            holder.getBinding().setListener(listener);
                        }
                    })
                    .into(recyclerView);
            recyclerView.setTag(R.id.recycler_data, items);
            //recyclerView.setLayoutManager(new LinearPartLayoutManager(context, LinearLayoutManager.VERTICAL));
            final SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        } else {
            //noinspection unchecked
            items = ((List<Card>) recyclerView.getTag(R.id.recycler_data));
            DiffUtil.calculateDiff(new DiffUtilCallback(items, newItems), true)
                    .dispatchUpdatesTo(recyclerView.getAdapter());
            items.clear();
            items.addAll(newItems);
        }
    }

    public void setDebts(List<Debt> items) {
        this.debts.set(items);
    }

    public void setCards(List<Card> items) {
        this.cards.set(items);
    }
}
