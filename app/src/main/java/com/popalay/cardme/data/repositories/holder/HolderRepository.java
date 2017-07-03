package com.popalay.cardme.data.repositories.holder;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Singleton
public class HolderRepository {

    @Inject public HolderRepository() {}

    public Flowable<List<Holder>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Holder.class)
                .findAllSorted(Holder.NAME));
    }

    public Flowable<Holder> get(long holderId) {
        return RxRealm.listenElement(realm -> realm.where(Holder.class)
                .equalTo(Holder.ID, holderId).findAll());
    }

    public Completable updateCounts(Holder holder) {
        return RxRealm.doTransactional(realm -> {
            final long cardCount = realm.where(Card.class).equalTo(Card.HOLDER_ID, holder.getId()).count();
            final long debtCount = realm.where(Debt.class).equalTo(Debt.HOLDER_ID, holder.getId()).count();
            if (cardCount <= 0 && debtCount <= 0) {
                realm.where(Holder.class).equalTo(Holder.ID, holder.getId()).findAll().deleteAllFromRealm();
            } else {
                holder.setCardsCount((int) cardCount);
                holder.setDebtCount((int) debtCount);
                realm.copyToRealmOrUpdate(holder);
            }
        });
    }

    public Completable remove(Holder holder) {
        return RxRealm.doTransactional(realm ->
                realm.where(Holder.class).equalTo(Holder.ID, holder.getId())
                        .findAll().deleteAllFromRealm());
    }
}
