package com.popalay.yocard.ui.adddebt;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddDebtPresenter extends BasePresenter<AddDebtView> {

    private static final String TAG = "AddDebtPresenter";

    @Inject CardsInteractor cardsInteractor;

    public AddDebtPresenter() {
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        cardsInteractor.getAutoCompletedCardHoldersName()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleBaseError);
    }

    public void onSaveClick(Debt debt) {
        //TODO save debt
        getViewState().close();
    }
}
