package com.popalay.cardme.data.repository.device

import android.content.Context
import android.content.pm.PackageManager
import com.github.tamir7.contacts.Contact
import com.popalay.cardme.domain.repository.DeviceRepository
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataDeviceRepository @Inject constructor(
        private val context: Context,
        private val contactProvider: ContactProvider
) : DeviceRepository {

    override fun supportNfc() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

    override fun getContacts(): List<Contact> = contactProvider.getContacts()

    override fun checkPermissions(vararg permissions: String): Flowable<Boolean> = PermissionChecker.check(context, *permissions)

}
