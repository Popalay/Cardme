package com.popalay.cardme.utils

import android.content.Context
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack
import com.kcode.permissionslib.main.PermissionCompat
import com.popalay.cardme.business.exception.ExceptionFactory
import io.reactivex.*

object PermissionChecker {

    fun <T> composeSingle(context: Context, permission: String): SingleTransformer<T, T> =
            composeSingle(context, arrayOf(permission))


    fun <T> compose(context: Context, permission: String): FlowableTransformer<T, T> =
            compose(context, arrayOf(permission))

    fun <T> composeSingle(context: Context, permissions: Array<String>): SingleTransformer<T, T> =
            SingleTransformer {
                checkSingle(context, permissions)
                        .flatMap { granted ->
                            if (granted) it
                            else Single.error<T>(ExceptionFactory
                                    .createError(ExceptionFactory.ErrorType.PERMISSION_DENIED))
                        }
            }

    fun <T> compose(context: Context, permissions: Array<String>): FlowableTransformer<T, T> =
            FlowableTransformer {
                check(context, permissions)
                        .flatMap { granted ->
                            if (granted) it
                            else Flowable.error<T>(ExceptionFactory
                                    .createError(ExceptionFactory.ErrorType.PERMISSION_DENIED))
                        }
            }

    fun check(context: Context, permission: String): Flowable<Boolean> = check(context, arrayOf(permission))

    fun checkSingle(context: Context, permission: String): Single<Boolean> = checkSingle(context, arrayOf(permission))

    fun check(context: Context, permissions: Array<String>): Flowable<Boolean> =
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

    fun checkSingle(context: Context, permissions: Array<String>): Single<Boolean> =
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
