package com.popalay.yocard.ui.removablelistitem;

import com.popalay.yocard.ui.base.BasePresenter;

import java.util.List;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;

public abstract class RemovableListItemPresenter<T, V extends RemovableListItemView<T>> extends BasePresenter<V> {

    private List<T> items;

    public void onItemSwiped(T item, int position) {
        items.remove(item);
        getViewState().setItems(items);
        getViewState().showRemoveUndoAction(item, position);
    }

    public void onRemoveUndoActionDismissed(T item, int position) {
        removeItem(item)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    public void onRemoveUndo(T item, int position) {
        items.add(position, item);
        getViewState().setItems(items);
    }

    protected void setItems(List<T> items) {
        this.items = items;
    }

    protected abstract Completable removeItem(T item);
}
