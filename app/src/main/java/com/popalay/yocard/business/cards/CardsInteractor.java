package com.popalay.yocard.business.cards;

import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.repositories.CardRepository;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.popalay.yocard.data.models.Card.CARD_COLOR_GREY;
import static com.popalay.yocard.data.models.Card.CARD_COLOR_PURPLE;

@Singleton
public class CardsInteractor {

    private final CardRepository cardRepository;

    @Inject
    public CardsInteractor(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void save(Card card) {
        //noinspection WrongConstant
        card.setColor(new Random().nextInt(CARD_COLOR_PURPLE + 1 - CARD_COLOR_GREY) + CARD_COLOR_GREY);
        cardRepository.save(card);
    }

    public Observable<List<Card>> getCards() {
        return cardRepository.getCards().subscribeOn(Schedulers.io());
    }

    public Observable<List<String>> getAutoCompletedCardHoldersName() {
        return cardRepository.getCardHolders().subscribeOn(Schedulers.io());
    }

    public Completable copyCard(Card card) {
        return cardRepository.copyToClipboard(card)
                .observeOn(Schedulers.io())
                .andThen(cardRepository.incCardUsage(card));
    }
}
