package com.popalay.cardme.presentation.base

import android.support.v4.app.Fragment
import com.popalay.cardme.injection.Injectable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment(), Injectable {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

}
