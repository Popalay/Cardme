package com.popalay.cardme.data.repositories.device

import android.content.Context
import android.content.pm.PackageManager
import com.github.tamir7.contacts.Contact
import com.popalay.cardme.data.PermissionChecker
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
        private val context: Context,
        private val contactProvider: ContactProvider
) {

    fun supportNfc() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

    fun getContacts(): List<Contact> = contactProvider.getContacts()

    fun checkPermissions(vararg permissions: String): Flowable<Boolean> = PermissionChecker.check(context, *permissions)

}
