package com.popalay.cardme.data.repositories.debt;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.models.Holder;

import java.util.List;

import rx.Completable;
import rx.Observable;

public class DefaultDebtRepository implements DebtRepository {

    @Override public Completable save(Debt debt) {
        return Completable.fromAction(() -> RxRealm.generateObjectId(debt, (realm, id) -> {
            if (debt.getId() == 0) {
                debt.setId(id);
            }
            final Holder realmHolder = realm.where(Holder.class).equalTo(Holder.NAME, debt.getHolder().getName())
                    .findFirst();
            if (realmHolder != null) {
                debt.setHolder(realmHolder);
            } else {
                final Number num = realm.where(Holder.class).max(Holder.ID);
                final long nextID = num != null ? num.longValue() + 1L : 0L;
                debt.getHolder().setId(nextID);
            }
            debt.getHolder().setDebtCount(debt.getHolder().getDebtCount() + 1);
            if (debt.getCreatedAt() == 0L) {
                debt.setCreatedAt(System.currentTimeMillis());
            }
            realm.copyToRealmOrUpdate(debt);
        }));
    }

    @Override public Observable<List<Debt>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Debt.class)
                .findAllSorted(Debt.CREATED_AT));
    }

    @Override public Observable<List<Debt>> getAllByHolder(long holderId) {
        return RxRealm.listenList(realm -> realm.where(Debt.class)
                .equalTo(Debt.HOLDER_ID, holderId)
                .findAllSorted(Debt.CREATED_AT));
    }

    @Override public Observable<Holder> get(long id) {
        return RxRealm.listenElement(realm -> realm.where(Holder.class)
                .equalTo(Debt.ID, id)
                .findAll());
    }

    @Override public Completable remove(Debt debt) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm ->
                realm.where(Debt.class).equalTo(Debt.ID, debt.getId()).findAll().deleteAllFromRealm()));
    }
}
