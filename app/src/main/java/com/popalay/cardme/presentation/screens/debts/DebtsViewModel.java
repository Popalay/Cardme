package com.popalay.cardme.presentation.screens.debts;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.utils.recycler.DiffUtilCallback;
import com.popalay.cardme.utils.recycler.decoration.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DebtsViewModel {

    public final ObservableField<List<Debt>> debts = new ObservableField<>();

    @BindingAdapter(value = {"debts", "itemClickListener"}, requireAll = false)
    public static void setDebts(RecyclerView recyclerView, List<Debt> newItems, ItemClickListener listener) {
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

    public void setDebts(List<Debt> items) {
        debts.set(items);
    }

    public Debt get(int position) {
        return debts.get().get(position);
    }

}
