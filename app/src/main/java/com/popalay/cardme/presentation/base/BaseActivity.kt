package com.popalay.cardme.presentation.base

import android.app.ProgressDialog
import android.support.annotation.StringRes
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.popalay.cardme.R
import com.popalay.cardme.injection.Injectable
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.DialogFactory
import com.popalay.cardme.utils.ViewUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

abstract class BaseActivity : MvpAppCompatActivity(), BaseView, Injectable{

    @Inject lateinit var navigationHolder: NavigatorHolder

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }
    protected open val navigator = CustomNavigator(this)

    private val progressDialog: ProgressDialog by lazy {
        DialogFactory.createProgressDialog(this, R.string.please_wait)
    }

    private val subscriptions = CompositeDisposable()

    fun showLoadingDialog() = progressDialog.show()

    fun addDisposable(disposable: Disposable) {
        if (disposable.isDisposed) return
        subscriptions.add(disposable)
    }

    fun hideLoadingDialog() = progressDialog.dismiss()

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

    override fun showError(error: String) = DialogFactory.createSimpleOkErrorDialog(this, error).show()

    override fun showError(@StringRes error: Int) = showError(getString(error))

    override fun showMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun hideError() {}

    override fun hideMessage() {}

    override fun showProgress() = showLoadingDialog()

    override fun hideProgress() = hideLoadingDialog()

    override fun hideKeyboard() = ViewUtil.hideKeyboard(this)

    override fun close() = finish()
}
