package com.popalay.yocard.ui.removablelistitem;

import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

public interface RemovableListItemView<T> extends BaseView {

    void showRemoveUndoAction(T item, int position);

    void setItems(List<T> items);
}
