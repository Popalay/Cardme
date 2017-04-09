package com.popalay.cardme.data.repositories;

import android.content.Context;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Sort;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class CardRepository {

    private final Context context;

    @Inject
    public CardRepository(Context context) {
        this.context = context;
    }

    public Completable save(Card card) {
        return Completable.fromAction(() -> RxRealm.generateObjectId(card, (realm, id) -> {
            if (card.getId() > 0) {
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
            card.getHolder().setCardsCount(card.getHolder().getCardsCount() + 1);
            realm.copyToRealmOrUpdate(card);
        }));
    }

    public Observable<List<Card>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .findAllSorted(Card.ID, Sort.DESCENDING, Card.USAGE, Sort.DESCENDING)
                .sort(Card.USAGE, Sort.DESCENDING));
    }

    public Observable<List<Card>> getAllByHolder(long holderId) {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .equalTo(Card.HOLDER_ID, holderId)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.USAGE, Sort.DESCENDING));
    }

    public Completable incCardUsage(final Card card) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            card.setUsage(card.getUsage() + 1);
            realm.copyToRealmOrUpdate(card);
        }));
    }

    public Completable remove(final Card card) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            realm.where(Card.class).equalTo(Card.ID, card.getId()).findAll().deleteAllFromRealm();
        }));
    }

    public Single<Integer> countByHolder(Holder holder) {
        return RxRealm.getList(realm -> realm.where(Card.class).equalTo(Card.HOLDER_ID, holder.getId()).findAll())
                .map(List::size);
    }
}
