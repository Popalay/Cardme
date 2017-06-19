package com.popalay.cardme.data.repositories.card;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Holder;

import java.util.List;

import io.realm.Sort;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class DefaultCardRepository implements CardRepository {

    @Override public Completable save(Card card) {
        return Completable.fromAction(() -> RxRealm.generateObjectId(card, (realm, id) -> {
            if (card.getId() == 0) {
                card.setId(id);
            }
            final Holder realmHolder = realm.where(Holder.class).equalTo(Holder.NAME, card.getHolder().getName())
                    .findFirst();
            if (realmHolder != null) {
                card.setHolder(realmHolder);
            } else {
                final Number num = realm.where(Holder.class).max(Holder.ID);
                final long nextID = num != null ? num.longValue() + 1L : 0L;
                card.getHolder().setId(nextID);
            }
            realm.copyToRealmOrUpdate(card);
        }));
    }

    @Override public Completable save(List<Card> cards) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> realm.copyToRealmOrUpdate(cards)));
    }

    @Override public Observable<List<Card>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.USAGE));
    }

    @Override public Observable<List<Card>> getAllByHolder(long holderId) {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .equalTo(Card.HOLDER_ID, holderId)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.USAGE));
    }

    @Override public Completable remove(final Card card) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            realm.where(Card.class).equalTo(Card.ID, card.getId()).findAll().deleteAllFromRealm();
        }));
    }

    @Override public Single<Boolean> isCardExist(Card card) {
        return RxRealm.getElement(realm -> realm.where(Card.class)
                .equalTo(Card.FORMATTED_NUMBER, card.getNumber())
                .findFirst())
                .map(finded -> finded != null);
    }
}
