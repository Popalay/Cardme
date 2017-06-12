package com.popalay.cardme.ui.screens.debts;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.ui.screens.removablelistitem.RemovableListItemPresenter;

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

    @Override
    protected Completable saveItem(Debt item) {
        return debtsInteractor.save(item);
    }
}
