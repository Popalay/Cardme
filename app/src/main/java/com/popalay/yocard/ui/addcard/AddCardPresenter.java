package com.popalay.yocard.ui.addcard;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;

@InjectViewState
public class AddCardPresenter extends BasePresenter<AddCardView> {

    @Inject CardsInteractor cardsInteractor;

    public AddCardPresenter(CreditCard card) {
        App.appComponent().inject(this);

        getViewState().showCardDetails(new Card(card));
        cardsInteractor.getAutoCompletedCardHoldersName()
                .compose(bindToLifecycle())
                .subscribe(getViewState()::setCompletedCardHolders, this::handleBaseError);
    }

    public void onAcceptClick(Card card) {
        cardsInteractor.save(card);
        getViewState().close();
    }
}
