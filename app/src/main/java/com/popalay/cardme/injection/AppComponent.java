package com.popalay.cardme.injection;

import com.popalay.cardme.ui.screens.addcard.AddCardPresenter;
import com.popalay.cardme.ui.screens.adddebt.AddDebtPresenter;
import com.popalay.cardme.ui.screens.cards.CardsPresenter;
import com.popalay.cardme.ui.screens.cards.CardsViewModel;
import com.popalay.cardme.ui.screens.debts.DebtsPresenter;
import com.popalay.cardme.ui.screens.holderdetails.HolderDetailsPresenter;
import com.popalay.cardme.ui.screens.holders.HoldersPresenter;
import com.popalay.cardme.ui.screens.settings.SettingPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class
})
public interface AppComponent {

    void inject(AddCardPresenter presenter);

    void inject(CardsPresenter presenter);

    void inject(HoldersPresenter presenter);

    void inject(HolderDetailsPresenter presenter);

    void inject(AddDebtPresenter presenter);

    void inject(DebtsPresenter presenter);

    void inject(SettingPresenter presenter);

    void inject(CardsViewModel viewModel);
}