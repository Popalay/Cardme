package com.popalay.yocard.ui.holdercards;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.R;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.business.holders.HoldersInteractor;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HolderCardsPresenter extends BasePresenter<HolderCardsView> {

    @Inject CardsInteractor cardsInteractor;
    @Inject HoldersInteractor holdersInteractor;
    @Inject Context context;

    private long holderId;

    public HolderCardsPresenter(long holderId) {
        this.holderId = holderId;
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        cardsInteractor.getHolderCards(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCards, this::handleBaseError);

        holdersInteractor.getHolderName(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setHolderName, this::handleBaseError);
    }

    public void onCardClick(Card card) {
        cardsInteractor.copyCard(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().showMessage(context.getString(R.string.number_copied)),
                        this::handleBaseError);
    }

    public void onCardSwiped(Card card, int position) {
        getViewState().removeCard(position);
        getViewState().showRemoveUndoAction(card, position);
    }

    public void onRemoveUndoActionDismissed(Card card, int position) {
        cardsInteractor.removeCard(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    public void onRemoveUndo(Card card, int position) {
        getViewState().resetCard(card, position);
    }
}
