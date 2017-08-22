package com.popalay.cardme.data.repositories

import android.content.Context
import android.content.pm.PackageManager
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts
import com.popalay.cardme.data.PermissionChecker
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
        private val context: Context
) {

    fun supportNfc() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

    fun getContacts(): List<Contact?> = Contacts.getQuery().find()

    fun checkPermissions(vararg permissions: String): Flowable<Boolean> = PermissionChecker.check(context, *permissions)

}
