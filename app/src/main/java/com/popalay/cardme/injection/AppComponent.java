package com.popalay.cardme.injection;

import com.popalay.cardme.presentation.screens.addcard.AddCardPresenter;
import com.popalay.cardme.presentation.screens.adddebt.AddDebtPresenter;
import com.popalay.cardme.presentation.screens.cards.CardsPresenter;
import com.popalay.cardme.presentation.screens.cards.CardsViewModel;
import com.popalay.cardme.presentation.screens.debts.DebtsPresenter;
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity;
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsViewModel;
import com.popalay.cardme.presentation.screens.holders.HoldersPresenter;
import com.popalay.cardme.presentation.screens.settings.SettingPresenter;
import com.popalay.cardme.presentation.screens.splash.SplashPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(AddCardPresenter presenter);

    void inject(CardsPresenter presenter);

    void inject(HoldersPresenter presenter);

    void inject(AddDebtPresenter presenter);

    void inject(DebtsPresenter presenter);

    void inject(SettingPresenter presenter);

    void inject(CardsViewModel viewModel);

    void inject(HolderDetailsViewModel viewModel);

    void inject(HolderDetailsActivity activity);

    void inject(SplashPresenter presenter);
}