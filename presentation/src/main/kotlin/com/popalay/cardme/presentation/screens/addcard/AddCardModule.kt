package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class AddCardModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(AddCardViewModel::class)
    abstract fun bindsAddCardViewModel(addCardViewModel: AddCardViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @PerActivity
        @JvmStatic fun provideViewModelFacade(viewModel: AddCardViewModel): AddCardViewModelFacade = viewModel

        @Provides
        @PerActivity
        @Named(AddCardActivity.KEY_CARD_NUMBER)
        @JvmStatic fun provideCardNumber(activity: AddCardActivity): String =
                activity.intent.getStringExtra(AddCardActivity.KEY_CARD_NUMBER)

    }

}