package com.popalay.yocard.business.debts;

import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.repositories.DebtsRepository;
import com.popalay.yocard.data.repositories.HolderRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class DebtsInteractor {

    private final DebtsRepository debtsRepository;
    private final HolderRepository holderRepository;

    @Inject
    public DebtsInteractor(DebtsRepository debtsRepository, HolderRepository holderRepository) {
        this.debtsRepository = debtsRepository;
        this.holderRepository = holderRepository;
    }

    public Completable save(Debt debt) {
        return debtsRepository.save(debt)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Debt>> getDebts() {
        return debtsRepository.getAll()
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Debt>> getDebtsByHolder(long holderId) {
        return debtsRepository.getAllByHolder(holderId)
                .subscribeOn(Schedulers.io());
    }

    public Completable remove(Debt debt) {
        return debtsRepository.remove(debt)
                .andThen(holderRepository.updateCounts(debt.getHolder()))
                .subscribeOn(Schedulers.io());
    }
}
