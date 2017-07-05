package com.popalay.cardme.presentation.base

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpAppCompatFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    private val subscriptions = CompositeDisposable()

    fun baseActivity(): BaseActivity {
        val activity = activity
        if (activity != null && activity is BaseActivity) {
            return activity
        }
        throw RuntimeException("BaseActivity is null")
    }

    override fun onDestroyView() {
        subscriptions.clear()
        super.onDestroyView()
    }

    fun addDisposable(disposable: Disposable) {
        if (disposable.isDisposed) return
        subscriptions.add(disposable)
    }

    protected fun showLoadingDialog() = baseActivity().showLoadingDialog()

    protected fun hideLoadingDialog() = baseActivity().hideLoadingDialog()

    override fun showError(error: String) = baseActivity().showError(error)

    override fun showError(@StringRes error: Int) = showError(getString(error))

    override fun showMessage(message: String) = baseActivity().showMessage(message)

    override fun hideError() {}

    override fun hideMessage() {}

    override fun showProgress() = showLoadingDialog()

    override fun hideProgress() = hideLoadingDialog()

    override fun hideKeyboard() = baseActivity().hideKeyboard()

    override fun close(): Unit = throw UnsupportedOperationException("You can not close the fragment")

}
