package com.popalay.yocard.ui.debts;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.debts.DebtsInteractor;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class DebtsPresenter extends BasePresenter<DebtsView> {

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
                .subscribe(getViewState()::setDebts, this::handleBaseError);
    }

    public void onAddClick() {
        getViewState().showAddDialog();
    }

    public void onDebtSwiped(Card card, int position) {
        //TODO Remove
    }

    public void onRemoveUndoActionDismissed(Card card, int position) {
        //TODO
    }

    public void onRemoveUndo(Card card, int position) {
        // TODO
    }
}
