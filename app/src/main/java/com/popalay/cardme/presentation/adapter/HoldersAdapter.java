package com.popalay.cardme.presentation.adapter;

import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.ItemHolderBinding;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.utils.recycler.adapter.BaseDataBoundAdapter;
import com.popalay.cardme.utils.recycler.adapter.DataBoundViewHolder;

import java.util.List;

public class HoldersAdapter extends BaseDataBoundAdapter<ItemHolderBinding, Holder> {

    private ItemClickListener<Holder> itemClickListener;

    public void setItemClickListener(ItemClickListener<Holder> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override protected void bindItem(DataBoundViewHolder<ItemHolderBinding> vh,
            int position, List<Object> payloads) {
        final Holder holder = getItem(position);
        vh.binding.setItem(holder);
        vh.binding.setListener(itemClickListener);
    }

    @Override public int getItemLayoutId(int position) { return R.layout.item_holder; }
}