package com.popalay.cardme.presentation.screens.removablelistitem;

import com.popalay.cardme.presentation.base.BaseView;

public interface RemovableListItemView<T> extends BaseView {

    void showRemoveUndoAction(T item);

}
