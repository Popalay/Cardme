package com.popalay.yocard.ui.debts;

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
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.databinding.ItemDebtBinding;
import com.popalay.yocard.ui.base.ItemClickListener;
import com.popalay.yocard.utils.recycler.DiffUtilCallback;
import com.popalay.yocard.utils.recycler.HorizontalDividerItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DebtsViewModel {

    public final ObservableField<List<Debt>> debts = new ObservableField<>();

    @BindingAdapter(value = {"bind:debts", "bind:itemClickListener"}, requireAll = false)
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
            LastAdapter.with(items, BR.item, true)
                    .map(Debt.class, new ItemType<ItemDebtBinding>(R.layout.item_debt) {
                        @Override
                        public void onBind(@NotNull ItemDebtBinding binding, @NotNull View view, int position) {
                            super.onBind(binding, view, position);
                            //binding.setListener(listener);
                        }
                    })
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
        this.debts.set(items);
    }

}
