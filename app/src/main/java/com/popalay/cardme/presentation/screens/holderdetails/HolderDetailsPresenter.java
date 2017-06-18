package com.popalay.cardme.presentation.screens.holderdetails;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.presentation.base.ViewModelPresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HolderDetailsPresenter extends ViewModelPresenter<HolderDetailsView, HolderDetailsViewModel> {

    @Inject CardInteractor cardInteractor;
    @Inject HolderInteractor holderInteractor;
    @Inject DebtsInteractor debtsInteractor;
    @Inject SettingsInteractor settingsInteractor;

    public HolderDetailsPresenter(long holderId) {
        super();
        App.appComponent().inject(this);

        cardInteractor.getCardsByHolder(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setCards, this::handleBaseError);

        debtsInteractor.getDebtsByHolder(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setDebts, this::handleBaseError);

        holderInteractor.getHolderName(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setHolderName, this::handleBaseError);

        settingsInteractor.listenSettings()
                .compose(bindToLifecycle())
                .map(Settings::isCardBackground)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setShowImage, this::handleBaseError);
    }

    @Override protected HolderDetailsViewModel createViewModel() {
        return new HolderDetailsViewModel();
    }

    public void onCardClick(Card card) {
        getViewState().shareCardNumber(card.getNumber());
    }
}
