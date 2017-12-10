package com.popalay.cardme.screens.debts

import android.app.ActivityOptions
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseFragment
import com.popalay.cardme.databinding.FragmentDebtsBinding
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.unsafeActivity
import com.popalay.cardme.utils.extensions.unsafeContext
import com.popalay.cardme.utils.transitions.FabTransform
import javax.inject.Inject

class DebtsFragment : BaseFragment() {

    companion object {
        fun newInstance() = DebtsFragment()
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: FragmentDebtsBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = getDataBinding<FragmentDebtsBinding>(inflater, R.layout.fragment_debts, container)
        b.vm = getViewModel<DebtsViewModel>(factory)
        return b.root
    }


    fun createAddDebtTransition(activityIntent: Intent): Bundle {
        FabTransform.addExtras(activityIntent, ContextCompat.getColor(unsafeContext, R.color.accent), R.drawable.ic_write)
        val options = ActivityOptions.makeSceneTransitionAnimation(unsafeActivity, b.buttonWrite, getString(R.string.transition_add_debt))
        return options.toBundle()
    }
}
