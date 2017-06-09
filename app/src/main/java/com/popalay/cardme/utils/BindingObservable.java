package com.popalay.cardme.utils;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import rx.Emitter;
import rx.Observable;

public class BindingObservable {

    private BindingObservable() {}

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
}
