package com.popalay.cardme.utils;

import android.content.Context;

import com.kcode.permissionslib.main.OnRequestPermissionsCallBack;
import com.kcode.permissionslib.main.PermissionCompat;
import com.popalay.cardme.business.exception.ExceptionFactory;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;

public final class PermissionChecker {

    private PermissionChecker() {
    }

    public static <T> SingleTransformer<T, T> composeSingle(Context context, String permission) {
        return composeSingle(context, new String[] {permission});
    }

    public static <T> FlowableTransformer<T, T> compose(Context context, String permission) {
        return compose(context, new String[] {permission});
    }

    public static <T> SingleTransformer<T, T> composeSingle(Context context, String[] permissions) {
        return single -> checkSingle(context, permissions)
                .flatMap(granted -> granted ? single :
                        Single.error(ExceptionFactory.createError(ExceptionFactory.ErrorType.PERMISSION_DENIED)));
    }

    public static <T> FlowableTransformer<T, T> compose(Context context, String[] permissions) {
        return observable -> check(context, permissions)
                .flatMap(granted -> granted ? observable :
                        Flowable.error(ExceptionFactory.createError(ExceptionFactory.ErrorType.PERMISSION_DENIED)));
    }

    public static Flowable<Boolean> check(Context context, String permission) {
        return check(context, new String[] {permission});
    }

    public static Single<Boolean> checkSingle(Context context, String permission) {
        return checkSingle(context, new String[] {permission});
    }

    public static Flowable<Boolean> check(Context context, String[] permissions) {
        return Flowable.create(emitter -> new PermissionCompat.Builder(context)
                .addPermissions(permissions)
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override public void onGrant() {
                        emitter.onNext(true);
                        emitter.onComplete();
                    }

                    @Override public void onDenied(String s) {
                        emitter.onNext(false);
                    }
                })
                .build()
                .request(), BackpressureStrategy.LATEST);
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
