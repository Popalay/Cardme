package com.popalay.yocard.ui.adddebt;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.debts.DebtsInteractor;
import com.popalay.yocard.business.holders.HoldersInteractor;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddDebtPresenter extends BasePresenter<AddDebtView> {

    @Inject DebtsInteractor debtsInteractor;
    @Inject HoldersInteractor holdersInteractor;

    public AddDebtPresenter() {
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        holdersInteractor.getHolderNames()
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
