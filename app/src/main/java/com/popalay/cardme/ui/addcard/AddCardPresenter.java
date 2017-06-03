package com.popalay.cardme.ui.addcard;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.exception.AppException;
import com.popalay.cardme.business.exception.ExceptionFactory;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddCardPresenter extends BasePresenter<AddCardView> {

    @Inject CardInteractor mCardInteractor;
    @Inject HolderInteractor holderInteractor;

    private final CreditCard creditCard;

    public AddCardPresenter(CreditCard card) {
        App.appComponent().inject(this);
        creditCard = card;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        mCardInteractor.transformCard(creditCard)
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showCardDetails, this::handleLocalError);

        holderInteractor.getHolderNames()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleLocalError);
    }

    public void onAcceptClick(Card card) {
        mCardInteractor.save(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::close, this::handleLocalError);
    }

    public void onErrorDialogDismiss() {
        getViewState().hideError();
    }

    public void onCloseClick() {
        getViewState().close();
    }

    private void handleLocalError(Throwable throwable) {
        if (ExceptionFactory.isAppException(throwable)) {
            handleAppError((AppException) throwable);
        } else {
            handleBaseError(throwable);
        }
    }

    private void handleAppError(AppException exception) {
        if (exception.getErrorType() == ExceptionFactory.ErrorType.CARD_EXIST) {
            getViewState().showError(exception.getMessageRes());
        }
    }
}
