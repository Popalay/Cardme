package com.popalay.cardme.utils.extensions

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseViewModel


internal fun FragmentActivity.currentFragment() = supportFragmentManager.fragments?.firstOrNull { it.isVisible }

internal inline fun <reified T : Fragment> AppCompatActivity.findFragmentByType() = supportFragmentManager.fragments
	?.filter { it is T }
	?.map { it as T }
	?.firstOrNull()

internal fun FragmentActivity.openShareChooser(@StringRes title: Int, text: String) {
	val intent = ShareCompat.IntentBuilder.from(this)
		.setChooserTitle(title)
		.setType("text/plain")
		.setText(text)
		.createChooserIntent()
	if (intent.resolveActivity(this.packageManager) != null) {
		this.startActivity(intent)
	}
}

internal fun <T : ViewDataBinding> getDataBinding(
	inflater: LayoutInflater?,
	@LayoutRes layoutId: Int,
	container: ViewGroup?
): T = DataBindingUtil.inflate(inflater, layoutId, container, false)

internal fun <T : ViewDataBinding> FragmentActivity.getDataBinding(
	@LayoutRes layoutId: Int
): T = DataBindingUtil.setContentView(this, layoutId)

internal inline fun <reified T : BaseViewModel> FragmentActivity.getViewModel(
	factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(application)
): T = ViewModelProviders.of(this, factory).get(T::class.java)

internal inline fun <reified T : ViewModel> FragmentActivity.bindViewModel(
	factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(application)
): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this, factory).get(T::class.java) }

internal inline fun <reified T : Any> Activity.extra(name: String): Lazy<T> =
	lazy(LazyThreadSafetyMode.NONE) {
		when (T::class) {
			String::class -> requireNotNull(intent.getStringExtra(name)) as T
			else -> throw IllegalArgumentException("${T::class} not supports")
		}
	}

internal inline fun <reified T : BaseViewModel> Fragment.getViewModel(
	factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(unsafeActivity.application)
): T = ViewModelProviders.of(this, factory).get(T::class.java)

internal fun Context.openLink(url: Uri) {
	val builder = CustomTabsIntent.Builder()
	builder.setToolbarColor(ContextCompat.getColor(this, R.color.primary))
	val customTabsIntent = builder.build()
	customTabsIntent.launchUrl(this, url)
}

internal fun Activity.makeSceneTransitionAnimation(vararg sharedElement: kotlin.Pair<View, String>) =
	ActivityOptionsCompat.makeSceneTransitionAnimation(
		this,
		*sharedElement.map { (view, name) -> Pair(view, name) }.toTypedArray()
	).toBundle() ?: Bundle.EMPTY

internal fun Context.openLink(url: String) = openLink(Uri.parse(url))