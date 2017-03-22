package com.popalay.yocard.ui.removablelistitem;

import com.popalay.yocard.ui.base.BaseView;

public interface RemovableListItemView<T> extends BaseView {

    void removeItem(int position);

    void resetItem(T item, int position);

    void showRemoveUndoAction(T item, int position);

}
