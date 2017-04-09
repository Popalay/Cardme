package com.popalay.cardme.ui.base;

import com.github.nitrico.lastadapter.StableId;

public interface ItemClickListener<T extends StableId> {

    void onItemClick(T item);
}
