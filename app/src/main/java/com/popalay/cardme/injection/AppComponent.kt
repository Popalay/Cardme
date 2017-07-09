package com.popalay.cardme.injection

import com.popalay.cardme.App
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.adddebt.AddDebtPresenter
import com.popalay.cardme.presentation.screens.cards.CardsPresenter
import com.popalay.cardme.presentation.screens.cards.CardsViewModel
import com.popalay.cardme.presentation.screens.debts.DebtsPresenter
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsViewModel
import com.popalay.cardme.presentation.screens.holders.HoldersFragment
import com.popalay.cardme.presentation.screens.holders.HoldersPresenter
import com.popalay.cardme.presentation.screens.holders.HoldersViewModel
import com.popalay.cardme.presentation.screens.home.HomeActivity
import com.popalay.cardme.presentation.screens.settings.SettingPresenter
import com.popalay.cardme.presentation.screens.splash.SplashPresenter
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.terrakok.cicerone.Router
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        ScreenModule::class,
        AppModule::class)
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: App): Builder
        fun build(): AppComponent
    }

    fun getRouter(): Router

    fun inject(presenter: CardsPresenter)

    fun inject(presenter: HoldersPresenter)

    fun inject(presenter: AddDebtPresenter)

    fun inject(presenter: DebtsPresenter)

    fun inject(presenter: SettingPresenter)

    fun inject(viewModel: CardsViewModel)

    fun inject(viewModel: HolderDetailsViewModel)

    fun inject(activity: HolderDetailsActivity)

    fun inject(presenter: SplashPresenter)

    fun inject(activity: AddCardActivity)

    fun inject(viewModel: HoldersViewModel)

    fun inject(fragment: HoldersFragment)

    fun inject(activity: HomeActivity)

}