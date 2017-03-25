package com.popalay.yocard.ui.debts;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.debts.DebtsInteractor;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.ui.removablelistitem.RemovableListItemPresenter;

import javax.inject.Inject;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class DebtsPresenter extends RemovableListItemPresenter<Debt, DebtsView> {

    @Inject DebtsInteractor debtsInteractor;
    @Inject Context context;

    public DebtsPresenter() {
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        debtsInteractor.getDebts()
                .compose(bindToLifecycle())
                .doOnNext(this::setItems)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setItems, this::handleBaseError);
    }

    public void onAddClick() {
        getViewState().showAddDialog();
    }

    @Override
    protected Completable removeItem(Debt item) {
        return debtsInteractor.remove(item);
    }
}
