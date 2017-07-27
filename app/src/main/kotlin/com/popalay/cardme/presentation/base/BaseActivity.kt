package com.popalay.cardme.presentation.base

import android.support.v7.app.AppCompatActivity
import com.popalay.cardme.injection.Injectable
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), Injectable {

    @Inject lateinit var navigationHolder: NavigatorHolder

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }
    public open var navigator = CustomNavigator(this)

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
