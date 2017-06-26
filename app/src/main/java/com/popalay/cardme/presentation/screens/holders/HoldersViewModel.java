package com.popalay.cardme.presentation.screens.holders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.ItemHolderBinding;
import com.popalay.cardme.presentation.base.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoldersViewModel {

    public final ObservableList<Holder> holders = new ObservableArrayList<>();

    public void setHolders(List<Holder> items) {
        final List<Holder> newList = new ArrayList<>(items);
        holders.clear();
        holders.addAll(newList);
    }

    @BindingAdapter(value = {"holdersAdapter", "itemClickListener"}, requireAll = false)
    public static void holdersAdapter(RecyclerView recyclerView, List<Holder> items,
            ItemClickListener listener) {
        //todo remove items null check
        if (recyclerView.getAdapter() != null || items == null) return;
        new LastAdapter(items, BR.item, true)
                .map(Holder.class, new ItemType<ItemHolderBinding>(R.layout.item_holder) {
                    @Override
                    public void onCreate(@NotNull com.github.nitrico.lastadapter.Holder<ItemHolderBinding> holder) {
                        super.onCreate(holder);
                        holder.getBinding().setListener(listener);
                    }
                })
                .into(recyclerView);
    }

}
