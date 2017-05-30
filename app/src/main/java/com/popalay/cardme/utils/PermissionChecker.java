package com.popalay.cardme.utils;

import android.content.Context;

import com.kcode.permissionslib.main.OnRequestPermissionsCallBack;
import com.kcode.permissionslib.main.PermissionCompat;
import com.popalay.cardme.business.exception.ExceptionFactory;

import rx.Emitter;
import rx.Observable;
import rx.Single;

public final class PermissionChecker {

    private PermissionChecker() {
    }

    public static <T> Single.Transformer<T, T> composeSingle(Context context, String permission) {
        return composeSingle(context, new String[] {permission});
    }

    public static <T> Observable.Transformer<T, T> compose(Context context, String permission) {
        return compose(context, new String[] {permission});
    }

    public static <T> Single.Transformer<T, T> composeSingle(Context context, String[] permissions) {
        return single -> checkSingle(context, permissions)
                .flatMap(granted -> granted ? single :
                        Single.error(ExceptionFactory.createError(ExceptionFactory.ErrorType.PERMISSION_DENIED)));
    }

    public static <T> Observable.Transformer<T, T> compose(Context context, String[] permissions) {
        return observable -> check(context, permissions)
                .flatMap(granted -> granted ? observable :
                        Observable.error(ExceptionFactory.createError(ExceptionFactory.ErrorType.PERMISSION_DENIED)));
    }

    public static Observable<Boolean> check(Context context, String permission) {
        return check(context, new String[] {permission});
    }

    public static Single<Boolean> checkSingle(Context context, String permission) {
        return checkSingle(context, new String[] {permission});
    }

    public static Observable<Boolean> check(Context context, String[] permissions) {
        return Observable.create(emitter -> new PermissionCompat.Builder(context)
                .addPermissions(permissions)
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override public void onGrant() {
                        emitter.onNext(true);
                        emitter.onCompleted();
                    }

                    @Override public void onDenied(String s) {
                        emitter.onNext(false);
                    }
                })
                .build()
                .request(), Emitter.BackpressureMode.LATEST);
    }

    public static Single<Boolean> checkSingle(Context context, String[] permissions) {
        return Single.create(subscriber -> new PermissionCompat.Builder(context)
                .addPermissions(permissions)
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override public void onGrant() {
                        subscriber.onSuccess(true);
                    }

                    @Override public void onDenied(String s) {
                        subscriber.onSuccess(false);
                    }
                })
                .build()
                .request());
    }
}
