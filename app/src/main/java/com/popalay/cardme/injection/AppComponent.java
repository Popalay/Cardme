package com.popalay.cardme.injection;

import com.popalay.cardme.ui.addcard.AddCardPresenter;
import com.popalay.cardme.ui.adddebt.AddDebtPresenter;
import com.popalay.cardme.ui.cards.CardsPresenter;
import com.popalay.cardme.ui.debts.DebtsPresenter;
import com.popalay.cardme.ui.holderdetails.HolderDetailsPresenter;
import com.popalay.cardme.ui.holders.HoldersPresenter;
import com.popalay.cardme.ui.settings.SettingPresenter;

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
}