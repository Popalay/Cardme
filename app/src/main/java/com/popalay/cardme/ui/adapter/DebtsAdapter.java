package com.popalay.cardme.ui.adapter;

import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.databinding.ItemDebtBinding;
import com.popalay.cardme.utils.recycler.adapter.BaseDataBoundAdapter;
import com.popalay.cardme.utils.recycler.adapter.DataBoundViewHolder;

import java.util.List;

public class DebtsAdapter extends BaseDataBoundAdapter<ItemDebtBinding, Debt> {

    @Override protected void bindItem(DataBoundViewHolder<ItemDebtBinding> vh,
            int position, List<Object> payloads) {
        final Debt debt = getItem(position);
        vh.binding.setItem(debt);
    }

    @Override public int getItemLayoutId(int position) { return R.layout.item_debt; }
}