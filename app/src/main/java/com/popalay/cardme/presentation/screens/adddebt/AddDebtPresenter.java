package com.popalay.cardme.presentation.screens.adddebt;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddDebtPresenter extends BasePresenter<AddDebtView> {

    @Inject DebtsInteractor debtsInteractor;
    @Inject HolderInteractor mHolderInteractor;

    public AddDebtPresenter() {
        App.appComponent().inject(this);

        getViewState().setViewModel(new AddDebtViewModel());
        mHolderInteractor.getHolderNames()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleBaseError);
    }

    public void onSaveClick(Debt debt) {
        debtsInteractor.save(debt)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::close, this::handleBaseError);
    }
}
