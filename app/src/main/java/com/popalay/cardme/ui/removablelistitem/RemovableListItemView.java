package com.popalay.cardme.ui.removablelistitem;

import com.popalay.cardme.ui.base.BaseView;

public interface RemovableListItemView<T> extends BaseView {

    void showRemoveUndoAction(T item);

}
