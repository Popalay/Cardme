package com.popalay.cardme.ui.holders;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.popalay.cardme.BR;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.ItemHolderBinding;
import com.popalay.cardme.ui.base.ItemClickListener;
import com.popalay.cardme.utils.recycler.DiffUtilCallback;
import com.popalay.cardme.utils.recycler.HorizontalDividerItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoldersViewModel {

    public final ObservableField<List<Holder>> holders = new ObservableField<>();

    @BindingAdapter(value = {"bind:holders", "bind:itemClickListener"}, requireAll = false)
    public static void setHolders(RecyclerView recyclerView, List<Holder> newItems, ItemClickListener listener) {
        if (newItems == null) {
            return;
        }
        final List<Holder> items;
        final Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() == null) {
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(context, R.color.grey, 1,
                    context.getResources().getDimensionPixelSize(R.dimen.title_offset), 0));
            items = new ArrayList<>(newItems);
            new LastAdapter(items, BR.item, true)
                    .map(Holder.class, new ItemType<ItemHolderBinding>(R.layout.item_holder) {
                        @Override
                        public void onCreate(@NotNull com.github.nitrico.lastadapter.Holder<ItemHolderBinding> holder) {
                            super.onCreate(holder);
                            holder.getBinding().setListener(listener);
                        }
                    })
                    .into(recyclerView);
            recyclerView.setTag(R.id.recycler_data, items);
        } else {
            //noinspection unchecked
            items = ((List<Holder>) recyclerView.getTag(R.id.recycler_data));
            DiffUtil.calculateDiff(new DiffUtilCallback(items, newItems), true)
                    .dispatchUpdatesTo(recyclerView.getAdapter());
            items.clear();
            items.addAll(newItems);
        }
    }

    public void setHolders(List<Holder> items) {
        this.holders.set(items);
    }

}
