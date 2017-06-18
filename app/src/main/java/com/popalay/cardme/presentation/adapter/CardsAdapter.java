package com.popalay.cardme.presentation.adapter;

import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.databinding.ItemCardBinding;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.utils.recycler.adapter.BaseDataBoundAdapter;
import com.popalay.cardme.utils.recycler.adapter.DataBoundViewHolder;

import java.util.List;

public class CardsAdapter extends BaseDataBoundAdapter<ItemCardBinding, Card> {

    private boolean showBackgrounds;
    private ItemClickListener<Card> itemClickListener;

    public void setShowBackgrounds(boolean showBackgrounds) {
        if (this.showBackgrounds == showBackgrounds) return;
        this.showBackgrounds = showBackgrounds;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener<Card> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override protected void bindItem(DataBoundViewHolder<ItemCardBinding> holder,
            int position, List<Object> payloads) {
        final Card card = getItem(position);
        holder.binding.setItem(card);
        holder.binding.setWithImage(showBackgrounds);
        holder.binding.setListener(itemClickListener);
    }

    @Override public int getItemLayoutId(int position) { return R.layout.item_card; }
}

