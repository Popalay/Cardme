package com.popalay.cardme.presentation.screens.removablelistitem;

import com.popalay.cardme.presentation.base.BasePresenter;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class RemovableListItemPresenter<T, V extends RemovableListItemView<T>> extends BasePresenter<V> {

    public void onItemSwiped(T item) {
        getViewState().showRemoveUndoAction(item);
        addDisposable(removeItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError));
    }

    public void onRemoveUndo(T item) {
        addDisposable(saveItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError));
    }

    protected abstract Completable removeItem(T item);

    protected abstract Completable saveItem(T item);
}
