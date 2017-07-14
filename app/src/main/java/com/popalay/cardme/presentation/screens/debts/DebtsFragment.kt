package com.popalay.cardme.presentation.screens.debts

import android.app.ActivityOptions
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.databinding.FragmentDebtsBinding
import com.popalay.cardme.presentation.base.BaseFragment
import javax.inject.Inject

class DebtsFragment : BaseFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: FragmentDebtsBinding
    private lateinit var viewModelFacade: DebtsViewModelFacade

    companion object {

        fun newInstance() = DebtsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = DataBindingUtil.inflate<FragmentDebtsBinding>(inflater, R.layout.fragment_debts, container, false)
        ViewModelProviders.of(this, factory).get(DebtsViewModel::class.java).let {
            b.vm = it
            viewModelFacade = it
        }
        initUI()
        return b.root
    }

    private fun initUI() {
        viewModelFacade.createAddDebtTransition()
                .map {
                    ActivityOptions.makeSceneTransitionAnimation(activity, b.buttonWrite,
                            getString(R.string.transition_add_debt)).toBundle()
                }
    }

}
