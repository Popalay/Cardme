package com.popalay.yocard.business.cards;

import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.repositories.CardRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class CardsInteractor {

    private final CardRepository cardRepository;

    @Inject
    public CardsInteractor(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void save(Card card) {
        cardRepository.save(card);
    }

    public Observable<List<Card>> getCards() {
        return cardRepository.getCards().subscribeOn(Schedulers.io());
    }

}
