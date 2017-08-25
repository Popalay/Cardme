package com.popalay.cardme.business

import android.Manifest
import com.github.tamir7.contacts.Contact
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.whenever
import com.popalay.cardme.business.interactor.HolderInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.data.repositories.DeviceRepository
import com.popalay.cardme.data.repositories.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
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

    @Test fun getAll_Success() {
        val holders = (1..5).map { Holder() }.toMutableList()
        val trashedHolder = Holder(isTrash = true)
        holders.add(trashedHolder)

        whenever(holderRepository.getAll()).thenReturn(Flowable.fromCallable {
            holders.remove(trashedHolder)
            holders
        })

        val testObserver = holderInteractor.getAll().test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).getAll()

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

    @Test fun get_Success() {
        val name = "Denis"
        val holder = Holder(name = name)

        whenever(holderRepository.get(name)).thenReturn(Flowable.just(holder))

        val testObserver = holderInteractor.get(name).test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).get(name)

        testObserver
                .assertNoErrors()
                .assertValue { it == holder }
                .assertComplete()
    }

    @Test fun addCard_Success() {
        val holder = Holder(name = "Denis")
        val card = Card(holder = holder)

        whenever(holderRepository.addCard(holder.name, card)).thenReturn(Completable.complete())

        val testObserver = holderInteractor.addCard(holder.name, card).test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).addCard(holder.name, card)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun addDebt_Success() {
        val holder = Holder(name = "Denis")
        val debt = Debt(holder = holder)

        whenever(holderRepository.addDebt(holder.name, debt)).thenReturn(Completable.complete())

        val testObserver = holderInteractor.addDebt(holder.name, debt).test()

        testObserver.awaitTerminalEvent()

        verify(holderRepository).addDebt(holder.name, debt)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun getNames_WithContacts() {
        val holders = (1..5).map { Holder(name = "Denis $it") }.toList()
        val contact = mock<Contact>()
        whenever(contact.displayName).thenReturn("Petya")
        val contacts = (1..5).map { contact }.toList()

        whenever(holderRepository.getAll()).thenReturn(Flowable.just(holders))
        whenever(deviceRepository.checkPermissions(Manifest.permission.READ_CONTACTS)).thenReturn(Flowable.just(true))
        whenever(deviceRepository.getContacts()).thenReturn(contacts)

        val testObserver = holderInteractor.getNames().test()

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

        val testObserver = holderInteractor.getNames().test()

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