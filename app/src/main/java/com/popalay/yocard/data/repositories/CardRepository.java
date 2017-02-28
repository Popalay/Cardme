package com.popalay.yocard.data.repositories;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.data.models.Card;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class CardRepository {

    @Inject
    public CardRepository() {
    }

    public void save(Card card) {
        RxRealm.doTransactional(realm -> {
            Number num = realm.where(Card.class).max("id");
            long nextID = (num != null) ? num.longValue() + 1 : 0;
            card.setId(nextID);
            realm.copyToRealm(card);
        });
    }

    public Flowable<List<Card>> getCards() {
        return RxRealm.listenList(realm -> realm.where(Card.class).findAll());
    }
}
