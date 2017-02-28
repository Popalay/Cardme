package com.popalay.yocard.business.cards;

import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.repositories.CardRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

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

    public Flowable<List<Card>> getCards() {
        return cardRepository.getCards()
                .map(this::transform)
                .subscribeOn(Schedulers.io());
    }

    private List<Card> transform(List<Card> cards) {
        return null;
    }
}
