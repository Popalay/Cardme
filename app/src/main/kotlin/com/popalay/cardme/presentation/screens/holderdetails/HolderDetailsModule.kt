package com.popalay.cardme.presentation.screens.holderdetails

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class HolderDetailsModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(HolderDetailsViewModel::class)
    abstract fun bindsHolderDetailsViewModel(viewModel: HolderDetailsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @PerActivity
        @Named(HolderDetailsActivity.KEY_HOLDER_DETAILS)
        @JvmStatic fun provideHolderName(activity: HolderDetailsActivity): String =
                activity.intent.getStringExtra(HolderDetailsActivity.KEY_HOLDER_DETAILS)

        @Provides
        @PerActivity
        @JvmStatic fun provideViewModelFacade(viewModel: HolderDetailsViewModel): HolderDetailsViewModelFacade = viewModel
    }

}