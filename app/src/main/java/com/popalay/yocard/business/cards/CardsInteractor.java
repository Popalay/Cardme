package com.popalay.yocard.business.cards;

import android.content.Context;

import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.repositories.CardRepository;
import com.popalay.yocard.data.repositories.HolderRepository;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.card.payment.CreditCard;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import static com.popalay.yocard.data.models.Card.CARD_COLOR_GREY;
import static com.popalay.yocard.data.models.Card.CARD_COLOR_PURPLE;

@Singleton
public class CardsInteractor {

    private final CardRepository cardRepository;
    private final HolderRepository holderRepository;
    private final Context context;

    @Inject
    public CardsInteractor(CardRepository cardRepository,
            HolderRepository holderRepository,
            Context context) {
        this.cardRepository = cardRepository;
        this.holderRepository = holderRepository;
        this.context = context;
    }

    public Single<Card> transformCard(CreditCard creditCard) {
        return Single.fromCallable(() -> {
            final Card card = new Card(creditCard);
            card.setColor(new Random().nextInt(CARD_COLOR_PURPLE + 1 - CARD_COLOR_GREY) + CARD_COLOR_GREY);
            return card;
        });
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

    public Completable incCardUsage(Card card) {
        return cardRepository.incCardUsage(card)
                .subscribeOn(Schedulers.io());
    }

    public Completable removeCard(Card card) {
        return cardRepository.remove(card)
                .andThen(holderRepository.updateCounts(card.getHolder()))
                .subscribeOn(Schedulers.io());
    }
}
