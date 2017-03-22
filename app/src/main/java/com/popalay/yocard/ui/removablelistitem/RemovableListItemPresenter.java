package com.popalay.yocard.ui.removablelistitem;

import com.popalay.yocard.ui.base.BasePresenter;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;

public abstract class RemovableListItemPresenter<T, V extends RemovableListItemView<T>> extends BasePresenter<V> {

    public void onItemSwiped(T item, int position) {
        getViewState().removeItem(position);
        getViewState().showRemoveUndoAction(item, position);
    }

    public void onRemoveUndoActionDismissed(T item, int position) {
        removeItem(item)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    public void onRemoveUndo(T item, int position) {
        getViewState().resetItem(item, position);
    }

    protected abstract Completable removeItem(T item);
}
