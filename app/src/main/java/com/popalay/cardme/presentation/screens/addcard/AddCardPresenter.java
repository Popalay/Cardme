package com.popalay.cardme.presentation.screens.addcard;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.exception.AppException;
import com.popalay.cardme.business.exception.ExceptionFactory;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddCardPresenter extends BasePresenter<AddCardView> {

    @Inject CardInteractor cardInteractor;
    @Inject HolderInteractor holderInteractor;
    @Inject SettingsInteractor settingsInteractor;

    private final AddCardViewModel viewModel;

    public AddCardPresenter(CreditCard creditCard) {
        App.appComponent().inject(this);

        viewModel = new AddCardViewModel();

        addDisposable(cardInteractor.transformCard(creditCard)
                .doOnSuccess(viewModel::setCard)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(card -> getViewState().showCardDetails(viewModel), this::handleLocalError));

        addDisposable(settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setShowImage, this::handleBaseError));

        addDisposable(holderInteractor.getHolderNames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleLocalError));
    }

    public void onAcceptClick(Card card) {
        addDisposable(cardInteractor.save(card)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::close, this::handleLocalError));
    }

    public void onErrorDialogDismiss() {
        getViewState().hideError();
    }

    public void onCloseClick() {
        getViewState().close();
    }

    private void handleLocalError(Throwable throwable) {
        if (ExceptionFactory.INSTANCE.isAppException(throwable)) {
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
