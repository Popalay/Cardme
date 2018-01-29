package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CardDetailsModule {

	@Binds @PerActivity
	abstract fun bindsNavigator(navigator: CardDetailsNavigator): CustomNavigator

	@Binds @IntoMap @PerActivity
	@ViewModelKey(CardDetailsViewModel::class)
	abstract fun bindsCardDetailsViewModel(viewModel: CardDetailsViewModel): ViewModel

	@Module
	companion object {

		@Binds @PerActivity @JvmStatic
		fun provideNdefReceiver(activity: CardDetailsActivity) = NdefReceiver(activity)
	}
}