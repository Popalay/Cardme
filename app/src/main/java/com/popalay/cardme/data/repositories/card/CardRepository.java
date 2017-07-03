package com.popalay.cardme.data.repositories.card;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Holder;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.realm.Sort;

@Singleton
public class CardRepository {

    @Inject CardRepository() {}

    public Completable save(Card card) {
        return RxRealm.doTransactional(realm -> {
            if (card.getId() == null) {
                card.setId(UUID.randomUUID().toString());
            }
            final Holder realmHolder = realm.where(Holder.class).equalTo(Holder.NAME, card.getHolder().getName())
                    .findFirst();
            if (realmHolder != null) {
                card.setHolder(realmHolder);
            } else {
                card.getHolder().setId(UUID.randomUUID().toString());
            }
            realm.copyToRealmOrUpdate(card);
        });
    }

    public Completable save(List<Card> cards) {
        return RxRealm.doTransactional(realm -> realm.copyToRealmOrUpdate(cards));
    }

    public Flowable<List<Card>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION));
    }

    public Flowable<List<Card>> getAllByHolder(String holderId) {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .equalTo(Card.HOLDER_ID, holderId)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION));
    }

    public Completable remove(final Card card) {
        return RxRealm.doTransactional(realm -> realm.where(Card.class)
                .equalTo(Card.ID, card.getId()).findAll().deleteAllFromRealm());
    }

    public Single<Boolean> cardIsNew(Card card) {
        return RxRealm.getElement(realm -> realm.where(Card.class)
                .equalTo(Card.FORMATTED_NUMBER, card.getNumber()).findFirst())
                .isEmpty();
    }
}
