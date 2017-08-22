package com.popalay.cardme.data

import android.content.Context
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import io.reactivex.*

object PermissionChecker {

    fun <T> composeSingle(context: Context, vararg permissions: String): SingleTransformer<T, T> =
            SingleTransformer {
                checkSingle(context, *permissions)
                        .flatMap { granted ->
                            if (granted) it
                            else Single.error<T>(ExceptionFactory
                                    .createError(ExceptionFactory.ErrorType.PERMISSION_DENIED))
                        }
            }

    fun <T> compose(context: Context, vararg permissions: String): FlowableTransformer<T, T> =
            FlowableTransformer {
                check(context, *permissions)
                        .flatMap { granted ->
                            if (granted) it
                            else Flowable.error<T>(ExceptionFactory
                                    .createError(ExceptionFactory.ErrorType.PERMISSION_DENIED))
                        }
            }

    fun check(context: Context, vararg permissions: String): Flowable<Boolean> =
            Flowable.create<Boolean>({
                PermissionCompat.Builder(context)
                        .addPermissions(permissions)
                        .addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                            override fun onGrant() {
                                it.onNext(true)
                                it.onComplete()
                            }

                            override fun onDenied(s: String) {
                                it.onNext(false)
                            }
                        })
                        .build()
                        .request()
            }, BackpressureStrategy.LATEST)

    fun checkSingle(context: Context, vararg permissions: String): Single<Boolean> =
            Single.create<Boolean> {
                PermissionCompat.Builder(context)
                        .addPermissions(permissions)
                        .addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                            override fun onGrant() {
                                it.onSuccess(true)
                            }

                            override fun onDenied(s: String) {
                                it.onSuccess(false)
                            }
                        })
                        .build()
                        .request()
            }
}
