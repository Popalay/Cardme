package com.popalay.cardme.screens.holders

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.databinding.FragmentHoldersBinding
import com.popalay.cardme.base.BaseFragment
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import javax.inject.Inject

class HoldersFragment : BaseFragment() {

    companion object {
        fun newInstance() = HoldersFragment()
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: FragmentHoldersBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = getDataBinding<FragmentHoldersBinding>(inflater, R.layout.fragment_holders, container)
        b.vm = getViewModel<HoldersViewModel>(factory)
        return b.root
    }
}
