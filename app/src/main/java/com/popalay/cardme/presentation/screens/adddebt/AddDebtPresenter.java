package com.popalay.cardme.presentation.screens.adddebt;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddDebtPresenter extends BasePresenter<AddDebtView> {

    @Inject DebtsInteractor debtsInteractor;
    @Inject HolderInteractor holderInteractor;

    public AddDebtPresenter() {
        App.appComponent().inject(this);

        getViewState().setViewModel(new AddDebtViewModel());
        addDisposable(holderInteractor.getHolderNames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleBaseError));
    }

    public void onSaveClick(Debt debt) {
        addDisposable(debtsInteractor.save(debt)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::close, this::handleBaseError));
    }
}
