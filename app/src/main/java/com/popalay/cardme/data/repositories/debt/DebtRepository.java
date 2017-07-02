package com.popalay.cardme.data.repositories.debt;

import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.utils.RxRealm;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Singleton
public class DebtRepository {

    @Inject public DebtRepository() {}

    public Completable save(Debt debt) {
        return RxRealm.doTransactional(realm -> {
            if (debt.getId() == null) {
                debt.setId(UUID.randomUUID().toString());
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
            if (debt.getCreatedAt() == 0L) {
                debt.setCreatedAt(System.currentTimeMillis());
            }
            realm.copyToRealmOrUpdate(debt);
        });
    }

    public Flowable<List<Debt>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Debt.class)
                .findAllSorted(Debt.CREATED_AT));
    }

    public Flowable<List<Debt>> getAllByHolder(long holderId) {
        return RxRealm.listenList(realm -> realm.where(Debt.class)
                .equalTo(Debt.HOLDER_ID, holderId)
                .findAllSorted(Debt.CREATED_AT));
    }

    public Completable remove(Debt debt) {
        return RxRealm.doTransactional(realm -> realm.where(Debt.class).equalTo(Debt.ID, debt.getId())
                .findAll().deleteAllFromRealm());
    }
}
