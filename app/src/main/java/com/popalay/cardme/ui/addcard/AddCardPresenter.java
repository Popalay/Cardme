package com.popalay.cardme.ui.addcard;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardsInteractor;
import com.popalay.cardme.business.holders.HoldersInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class AddCardPresenter extends BasePresenter<AddCardView> {

    @Inject CardsInteractor cardsInteractor;
    @Inject HoldersInteractor holderInteractor;

    private final CreditCard creditCard;

    public AddCardPresenter(CreditCard card) {
        App.appComponent().inject(this);
        creditCard = card;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        cardsInteractor.transformCard(creditCard)
                .compose(bindToLifecycle().forSingle())
                .subscribe(getViewState()::showCardDetails, this::handleBaseError);

        holderInteractor.getHolderNames()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleBaseError);
    }

    public void onAcceptClick(Card card) {
        cardsInteractor.save(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::close, this::handleBaseError);
    }
}
