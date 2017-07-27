package com.popalay.cardme.business

import android.Manifest
import com.github.tamir7.contacts.Contact
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.whenever
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.data.repositories.device.DeviceRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

class HolderInteractorTest {

    private lateinit var deviceRepository: DeviceRepository
    private lateinit var holderRepository: HolderRepository
    private lateinit var holderInteractor: HolderInteractor

    @Before fun beforeEachTest() {
        deviceRepository = mock<DeviceRepository>()
        holderRepository = mock<HolderRepository>()
        holderInteractor = HolderInteractor(deviceRepository, holderRepository)
    }

    @Test fun getHolders_Success() {
        val holders = (1..5).map { Holder() }.toMutableList()
        val trashedHolder = Holder(isTrash = true)
        holders.add(trashedHolder)

        whenever(holderRepository.getAll()).thenReturn(Flowable.fromCallable {
            holders.remove(trashedHolder)
            holders
        })

        val testObserver = holderInteractor.getHolders().test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).getAll()

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

    @Test fun getHolder_Success() {
        val id = "ash111"
        val holder = Holder(id = id)

        whenever(holderRepository.get(id)).thenReturn(Flowable.just(holder))

        val testObserver = holderInteractor.getHolder(id).test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).get(id)

        testObserver
                .assertNoErrors()
                .assertValue { it == holder }
                .assertComplete()
    }

    @Test fun getFavoriteHolder_Success() {
        val holder = Holder()

        whenever(holderRepository.getWithMaxCounters()).thenReturn(Maybe.just(holder))

        val testObserver = holderInteractor.getFavoriteHolder().test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).getWithMaxCounters()

        testObserver
                .assertNoErrors()
                .assertValue { it == holder }
                .assertComplete()
    }

    @Test fun getHolderName_Success() {
        val id = "ash111"
        val name = "Denis"
        val holder = Holder(id = id, name = name)

        whenever(holderRepository.get(id)).thenReturn(Flowable.just(holder))

        val testObserver = holderInteractor.getHolderName(id).test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).get(id)

        testObserver
                .assertNoErrors()
                .assertValue { it == name }
                .assertComplete()
    }

    @Test fun getHolderNames_WithContacts() {
        val holders = (1..5).map { Holder(name = "Denis $it") }.toList()
        val contact = mock<Contact>()
        whenever(contact.displayName).thenReturn("Petya")
        val contacts = (1..5).map { contact }.toList()

        whenever(holderRepository.getAll()).thenReturn(Flowable.just(holders))
        whenever(deviceRepository.checkPermissions(Manifest.permission.READ_CONTACTS)).thenReturn(Flowable.just(true))
        whenever(deviceRepository.getContacts()).thenReturn(contacts)

        val testObserver = holderInteractor.getHolderNames().test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).getAll()
        verify(deviceRepository).checkPermissions(Manifest.permission.READ_CONTACTS)
        verify(deviceRepository).getContacts()

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 6 }
                .assertComplete()
    }

    @Test fun getHolderNames_WithoutContacts() {
        val holders = (1..5).map { Holder(name = "Denis $it") }.toList()
        val contact = mock<Contact>()
        whenever(contact.displayName).thenReturn("Petya")
        val contacts = (1..5).map { contact }.toList()

        whenever(holderRepository.getAll()).thenReturn(Flowable.just(holders))
        whenever(deviceRepository.checkPermissions(Manifest.permission.READ_CONTACTS)).thenReturn(Flowable.just(false))
        whenever(deviceRepository.getContacts()).thenReturn(contacts)

        val testObserver = holderInteractor.getHolderNames().test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).getAll()
        verify(deviceRepository).checkPermissions(Manifest.permission.READ_CONTACTS)
        verify(deviceRepository, never()).getContacts()

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

}