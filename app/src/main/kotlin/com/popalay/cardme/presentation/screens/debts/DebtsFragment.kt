package com.popalay.cardme.presentation.screens.debts

import android.app.ActivityOptions
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.databinding.FragmentDebtsBinding
import com.popalay.cardme.presentation.base.BaseFragment
import com.popalay.cardme.presentation.screens.adddebt.AddDebtActivity
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.transitions.FabTransform
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class DebtsFragment : BaseFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: FragmentDebtsBinding

    companion object {
        fun newInstance() = DebtsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = DataBindingUtil.inflate<FragmentDebtsBinding>(inflater, R.layout.fragment_debts, container, false)
        b.vm = ViewModelProviders.of(this, factory).get(DebtsViewModel::class.java)
        initUI()
        return b.root
    }

    private fun initUI() {
        //TODO fix communication
        b.vm!!.addDebtClickPublisher.applyThrottling()
                .subscribe {
                    val intent = AddDebtActivity.getIntent(activity)
                    FabTransform.addExtras(intent, ContextCompat.getColor(activity, R.color.accent), R.drawable.ic_write)
                    val options = ActivityOptions.makeSceneTransitionAnimation(activity, b.buttonWrite,
                            getString(R.string.transition_add_debt))
                    startActivity(intent, options.toBundle())
                }
                .addTo(disposables)
    }
}
