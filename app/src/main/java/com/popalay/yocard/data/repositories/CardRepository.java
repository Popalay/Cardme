package com.popalay.yocard.data.repositories;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.data.models.Card;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Sort;
import rx.Observable;

@Singleton
public class CardRepository {

    @Inject
    public CardRepository() {
    }

    public void save(Card card) {
        RxRealm.generateObjectId(card, (realm, id) -> {
            card.setId(id);
            realm.copyToRealm(card);
        });
    }

    public Observable<List<Card>> getCards() {
        return RxRealm.listenList(realm -> realm.where(Card.class).findAllSorted(Card.ID, Sort.DESCENDING));
    }

    public Observable<List<String>> getCardHolders() {
        return RxRealm.listenList(realm -> realm.where(Card.class).findAllSorted(Card.HOLDER_NAME))
                .map(cards -> Stream.of(cards).map(Card::getHolderName).collect(Collectors.toList()));
    }
}
