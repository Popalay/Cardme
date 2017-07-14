package com.popalay.cardme.utils;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class BindingUtils {

    private BindingUtils() {}

    public static <M> Flowable<M> create(ObservableField<M> observableField) {
        return Flowable.create(emitter -> {
            if (observableField.get() == null) return;
            emitter.onNext(observableField.get());
            final android.databinding.Observable.OnPropertyChangedCallback callback
                    = new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    emitter.onNext(observableField.get());
                }
            };
            observableField.addOnPropertyChangedCallback(callback);
            emitter.setCancellable(() -> observableField.removeOnPropertyChangedCallback(callback));
        }, BackpressureStrategy.LATEST);
    }

    public static Flowable<Boolean> create(ObservableBoolean observableField) {
        return Flowable.create(emitter -> {
            emitter.onNext(observableField.get());
            final android.databinding.Observable.OnPropertyChangedCallback callback
                    = new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    emitter.onNext(observableField.get());
                }
            };
            observableField.addOnPropertyChangedCallback(callback);
            emitter.setCancellable(() -> observableField.removeOnPropertyChangedCallback(callback));
        }, BackpressureStrategy.LATEST);
    }

    public static <M> void setItems(ObservableList<M> current, List<M> items) {
        final List<M> newList = items != null ? new ArrayList<>(items) : Collections.emptyList();
        current.clear();
        current.addAll(newList);
    }
}
