package com.popalay.cardme.data.repository.device

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.popalay.cardme.domain.repository.DeviceRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataDeviceRepository @Inject constructor(
        private val context: Context,
        private val contactProvider: ContactProvider
) : DeviceRepository {

    override fun supportNfc() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

    override fun getContactsNames(): Single<List<String>> =
            PermissionChecker.checkSingle(context, Manifest.permission.READ_CONTACTS)
                    .map { if (it) contactProvider.getContacts().map { it.displayName } else listOf() }

}
