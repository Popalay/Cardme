package com.popalay.cardme.presentation.screens.holderdetails

import android.arch.lifecycle.ViewModel
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
    @ViewModelKey(HolderDetailsViewModel::class)
    abstract fun bindsHolderDetailsViewModel(holderDetailsViewModel: HolderDetailsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @Named(HolderDetailsActivity.KEY_HOLDER_DETAILS)
        @JvmStatic fun provideHolderName(activity: HolderDetailsActivity): String =
                activity.intent.getStringExtra(HolderDetailsActivity.KEY_HOLDER_DETAILS)

    }

}