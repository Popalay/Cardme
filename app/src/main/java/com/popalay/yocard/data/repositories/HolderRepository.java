package com.popalay.yocard.data.repositories;

import android.content.Context;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Sort;
import rx.Observable;

@Singleton
public class HolderRepository {

    private final Context context;

    @Inject
    public HolderRepository(Context context) {
        this.context = context;
    }

    public void save(Holder holder) {
        RxRealm.generateObjectId(holder, (realm, id) -> {
            holder.setId(id);
            realm.copyToRealmOrUpdate(holder);
        });
    }

    public Observable<List<Holder>> getHolders() {
        return RxRealm.listenList(realm -> realm.where(Holder.class)
                .findAllSorted(Holder.ID, Sort.DESCENDING));
    }
}
