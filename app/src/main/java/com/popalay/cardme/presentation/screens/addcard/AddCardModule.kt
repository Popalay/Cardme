package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.card.payment.CreditCard

@Module
abstract class AddCardModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddCardViewModel::class)
    abstract fun bindsAddCardViewModel(addCardViewModel: AddCardViewModel): ViewModel


    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideCreditCard(activity: AddCardActivity): CreditCard =
                activity.intent.getParcelableExtra(AddCardActivity.KEY_CREDIT_CARD)

    }

}