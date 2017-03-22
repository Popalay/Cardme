package com.popalay.yocard.ui.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.yocard.BR;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.databinding.ItemDebtBinding;
import com.popalay.yocard.utils.recycler.DiffUtilCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DebtAdapterWrapper {

    private final LastAdapter adapter;
    private List<Debt> items;

    public DebtAdapterWrapper() {
        this.items = new ArrayList<>();
        this.adapter = LastAdapter.with(items, BR.item, true)
                .map(Debt.class, new ItemType<ItemDebtBinding>(R.layout.item_debt) {
                    @Override
                    public void onBind(@NotNull ItemDebtBinding binding, @NotNull View view, int position) {
                        super.onBind(binding, view, position);
                        //binding.setListener(listener);
                    }
                });
    }

    public void attachToRecycler(RecyclerView recyclerView) {
        this.adapter.into(recyclerView);
    }

    public void setItems(List<Debt> newItems) {
        final List<Debt> oldItems = new ArrayList<>(this.items);
        this.items.clear();
        this.items.addAll(newItems);
        if (oldItems.isEmpty()) {
            adapter.notifyDataSetChanged();
        } else {
            DiffUtil.calculateDiff(new DiffUtilCallback(oldItems, newItems), true)
                    .dispatchUpdatesTo(adapter);
        }
    }

    public Debt get(int position) {
        return items.get(position);
    }

    public void remove(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void add(Debt item, int position) {
        items.add(position, item);
        adapter.notifyItemInserted(position);
    }

    public void clear() {
        this.items.clear();
        this.adapter.notifyDataSetChanged();
    }
}
