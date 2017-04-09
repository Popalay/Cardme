package com.popalay.cardme.utils.recycler;

import android.support.v7.util.DiffUtil;

import com.github.nitrico.lastadapter.StableId;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

    private final List<? extends StableId> mOldItems;
    private final List<? extends StableId> mNewItems;

    public DiffUtilCallback(List<? extends StableId> oldItems, List<? extends StableId> newItems) {
        mOldItems = oldItems;
        mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems != null ? mOldItems.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewItems != null ? mNewItems.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).getStableId() == mNewItems.get(newItemPosition).getStableId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
    }
}
