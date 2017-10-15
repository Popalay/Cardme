package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import com.popalay.cardme.screens.home.CardDetailsNavigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class CardDetailsModule {

    @Binds
    @PerActivity
    abstract fun bindsNavigator(navigator: CardDetailsNavigator): CustomNavigator

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(CardDetailsViewModel::class)
    abstract fun bindsCardDetailsViewModel(viewModel: CardDetailsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @PerActivity
        @Named(CardDetailsActivity.KEY_CARD_NUMBER)
        @JvmStatic fun provideCardNumber(activity: CardDetailsActivity): String =
                activity.intent.getStringExtra(CardDetailsActivity.KEY_CARD_NUMBER)

        @Provides
        @PerActivity
        @JvmStatic fun provideViewModelFacade(viewModel: CardDetailsViewModel): CardDetailsViewModelFacade = viewModel
    }
}