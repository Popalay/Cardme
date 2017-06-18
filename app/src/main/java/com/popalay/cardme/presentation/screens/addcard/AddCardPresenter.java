package com.popalay.cardme.presentation.screens.addcard;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.exception.AppException;
import com.popalay.cardme.business.exception.ExceptionFactory;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddCardPresenter extends BasePresenter<AddCardView> {

    @Inject CardInteractor cardInteractor;
    @Inject HolderInteractor holderInteractor;
    @Inject SettingsInteractor settingsInteractor;

    private AddCardViewModel viewModel;

    public AddCardPresenter(CreditCard creditCard) {
        App.appComponent().inject(this);

        cardInteractor.transformCard(creditCard)
                .compose(bindToLifecycle().forSingle())
                .map(card -> viewModel = new AddCardViewModel(card))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showCardDetails, this::handleLocalError);

        settingsInteractor.listenSettings()
                .compose(bindToLifecycle())
                .map(Settings::isCardBackground)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(show -> viewModel.setShowImage(show), this::handleBaseError);

        holderInteractor.getHolderNames()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleLocalError);
    }

    public void onAcceptClick(Card card) {
        cardInteractor.save(card)
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
