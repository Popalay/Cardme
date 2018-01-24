package com.popalay.cardme.base

import android.support.v7.app.AppCompatActivity
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.injection.Injectable
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), Injectable {

	@Inject lateinit var navigationHolder: NavigatorHolder

	@Suppress("LeakingThis")
	protected open var navigator = CustomNavigator(this)

	private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

	override fun onResumeFragments() {
		super.onResumeFragments()
		navigationHolder.setNavigator(navigator)
	}

	override fun onPause() {
		navigationHolder.removeNavigator()
		super.onPause()
	}

	@CheckReturnValue fun <T> Observable<T>.bindToLifecycle() = autoDisposable(scopeProvider)
}
