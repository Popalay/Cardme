package com.popalay.cardme.ui.screens.removablelistitem;

import com.popalay.cardme.ui.base.BasePresenter;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;

public abstract class RemovableListItemPresenter<T, V extends RemovableListItemView<T>> extends BasePresenter<V> {

    public void onItemSwiped(T item) {
        getViewState().showRemoveUndoAction(item);
        removeItem(item)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    public void onRemoveUndo(T item) {
        saveItem(item)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    protected abstract Completable removeItem(T item);

    protected abstract Completable saveItem(T item);
}
