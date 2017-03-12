package com.popalay.yocard.ui.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.yocard.BR;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.databinding.ItemHolderBinding;
import com.popalay.yocard.ui.holders.HoldersView;
import com.popalay.yocard.utils.recycler.DiffUtilCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HolderAdapterWrapper {

    private final LastAdapter adapter;
    private List<Holder> items;

    public HolderAdapterWrapper(HolderListener listener) {
        this.items = new ArrayList<>();
        this.adapter = LastAdapter.with(items, BR.item, true)
                .map(Holder.class, new ItemType<ItemHolderBinding>(R.layout.item_holder) {
                    @Override
                    public void onBind(@NotNull ItemHolderBinding binding, @NotNull View view, int position) {
                        super.onBind(binding, view, position);
                        binding.setListener(listener);
                    }
                });
    }

    public void attachToRecycler(RecyclerView recyclerView) {
        this.adapter.into(recyclerView);
    }

    public void setItems(List<Holder> newItems) {
        final List<Holder> oldItems = new ArrayList<>(this.items);
        this.items.clear();
        this.items.addAll(newItems);
        if (oldItems.isEmpty()) {
            adapter.notifyDataSetChanged();
        } else {
            DiffUtil.calculateDiff(new DiffUtilCallback(oldItems, newItems), true)
                    .dispatchUpdatesTo(adapter);
        }
    }

    public Holder get(int position) {
        return items.get(position);
    }

    public void remove(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void add(Holder card, int position) {
        items.add(position, card);
        adapter.notifyItemInserted(position);
    }

    public void clear() {
        this.items.clear();
        this.adapter.notifyDataSetChanged();
    }

    public interface HolderListener {

        void onHolderClick(Holder holder);
    }
}
