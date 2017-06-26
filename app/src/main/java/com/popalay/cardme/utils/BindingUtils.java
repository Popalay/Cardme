package com.popalay.cardme.utils;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Emitter;
import rx.Observable;

public class BindingUtils {

    private BindingUtils() {}

    public static <M> Observable<M> create(ObservableField<M> observableField) {
        return Observable.create(emitter -> {
            emitter.onNext(observableField.get());
            final android.databinding.Observable.OnPropertyChangedCallback callback
                    = new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    emitter.onNext(observableField.get());
                }
            };
            observableField.addOnPropertyChangedCallback(callback);
            emitter.setCancellation(() -> observableField.removeOnPropertyChangedCallback(callback));
        }, Emitter.BackpressureMode.LATEST);
    }

    public static Observable<Boolean> create(ObservableBoolean observableField) {
        return Observable.create(emitter -> {
            emitter.onNext(observableField.get());
            final android.databinding.Observable.OnPropertyChangedCallback callback
                    = new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    emitter.onNext(observableField.get());
                }
            };
            observableField.addOnPropertyChangedCallback(callback);
            emitter.setCancellation(() -> observableField.removeOnPropertyChangedCallback(callback));
        }, Emitter.BackpressureMode.LATEST);
    }

    public static <M> void setItems(ObservableList<M> current, List<M> items) {
        final List<M> newList = items != null ? new ArrayList<>(items) : Collections.emptyList();
        current.clear();
        current.addAll(newList);
    }
}
