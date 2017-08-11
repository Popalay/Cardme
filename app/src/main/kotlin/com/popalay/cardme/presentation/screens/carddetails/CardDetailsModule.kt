package com.popalay.cardme.presentation.screens.carddetails

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class CardDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CardDetailsViewModel::class)
    abstract fun bindsCardDetailsViewModel(viewModel: CardDetailsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @Named(CardDetailsActivity.KEY_CARD_NUMBER)
        @JvmStatic fun provideCardNumber(activity: CardDetailsActivity): String =
                activity.intent.getStringExtra(CardDetailsActivity.KEY_CARD_NUMBER)

    }

}