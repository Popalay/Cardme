package com.popalay.cardme.business.cards;

import com.popalay.cardme.R;
import com.popalay.cardme.business.exception.ExceptionFactory;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.repositories.card.CardRepository;
import com.popalay.cardme.data.repositories.holder.HolderRepository;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.card.payment.CreditCard;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import static com.popalay.cardme.data.models.Card.CARD_COLOR_GREY;
import static com.popalay.cardme.data.models.Card.CARD_COLOR_PURPLE;

@Singleton
public class CardInteractor {

    private final CardRepository cardRepository;
    private final HolderRepository holderRepository;

    @Inject public CardInteractor(CardRepository cardRepository, HolderRepository holderRepository) {
        this.cardRepository = cardRepository;
        this.holderRepository = holderRepository;
    }

    public Single<Card> transformCard(CreditCard creditCard) {
        final Card card = transform(creditCard);
        return cardRepository.isCardExist(card)
                .flatMap(exist -> exist ? Single.error(createCardExistError()) : Single.just(card))
                .subscribeOn(Schedulers.io());
    }

    public Completable save(Card card) {
        return cardRepository.save(card)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Card>> getCards() {
        return cardRepository.getAll().subscribeOn(Schedulers.io());
    }

    public Observable<List<Card>> getCardsByHolder(long holderId) {
        return cardRepository.getAllByHolder(holderId).subscribeOn(Schedulers.io());
    }

    public Completable updateCardPositions(List<Card> items) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setUsage(i);
        }
        return cardRepository.update(items)
                .subscribeOn(Schedulers.io());
    }

    public Completable removeCard(Card card) {
        return cardRepository.remove(card)
                .andThen(holderRepository.updateCounts(card.getHolder()))
                .subscribeOn(Schedulers.io());
    }

    private Card transform(CreditCard creditCard) {
        final Card card = new Card(creditCard);
        card.setColor(new Random().nextInt(CARD_COLOR_PURPLE + 1 - CARD_COLOR_GREY) + CARD_COLOR_GREY);
        return card;
    }

    private Throwable createCardExistError() {
        return ExceptionFactory.createError(ExceptionFactory.ErrorType.CARD_EXIST, R.string.error_card_exist);
    }
}
