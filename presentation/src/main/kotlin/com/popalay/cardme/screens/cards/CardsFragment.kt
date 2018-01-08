package com.popalay.cardme.screens.cards

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.DataTransformers
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseFragment
import com.popalay.cardme.databinding.FragmentCardsBinding
import com.popalay.cardme.screens.carddetails.CardDetailsActivity
import com.popalay.cardme.utils.DialogFactory
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.utils.extensions.unsafeActivity
import com.popalay.cardme.utils.extensions.unsafeContext
import com.popalay.cardme.utils.recycler.SpacingItemDecoration
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class CardsFragment : BaseFragment() {

    companion object {
        const val SCAN_REQUEST_CODE = 121
        fun newInstance() = CardsFragment()
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var viewModelFacade: CardsViewModelFacade

    private lateinit var b: FragmentCardsBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = getDataBinding(inflater, R.layout.fragment_cards, container)
        b.vm = getViewModel(factory)
        initUI()
        return b.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
                viewModelFacade.onCardScanned(DataTransformers.transform(scanResult))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        b.buttonAdd.showAnimated()
    }

    override fun onPause() {
        super.onPause()
        b.buttonAdd.hideAnimated()
    }

    fun createCardDetailsTransition(activityIntent: Intent): Bundle {
        val position = viewModelFacade.getPositionOfCard(
                activityIntent.getStringExtra(CardDetailsActivity.KEY_CARD_NUMBER)
        )

        val transitions = mutableListOf<Pair<View, String>>()
        b.listCards.findViewHolderForAdapterPosition(position).itemView.let {
            val imageBackground = it.findViewById<View>(R.id.card_view)
            transitions += Pair(imageBackground, ViewCompat.getTransitionName(imageBackground))

            val imageType = it.findViewById<View>(R.id.image_card_type)
            transitions += Pair(imageType, ViewCompat.getTransitionName(imageType))

            val textTitle = it.findViewById<View>(R.id.text_title)
            transitions += Pair(textTitle, ViewCompat.getTransitionName(textTitle))

            val textNumber = it.findViewById<View>(R.id.text_number)
            transitions += Pair(textNumber, ViewCompat.getTransitionName(textNumber))

            val textHolderName = it.findViewById<View>(R.id.text_holder)
            transitions += Pair(textHolderName, ViewCompat.getTransitionName(textHolderName))
        }

        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                unsafeActivity,
                *transitions.toTypedArray()
        ).toBundle() ?: Bundle.EMPTY
    }

    private fun initUI() {
        b.listCards.addItemDecoration(SpacingItemDecoration.create {
            dividerSize = resources.getDimension(R.dimen.normal).toInt()
            showBetween = true
            showOnSides = true
        })

        viewModelFacade.doOnShowCardExistsDialog()
                .subscribe {
                    DialogFactory.createCustomButtonsDialog(
                            unsafeContext,
                            R.string.error_card_exist,
                            R.string.action_yes,
                            R.string.action_cancel,
                            onPositive = viewModelFacade::onWantToOverwrite,
                            onDismiss = viewModelFacade::onShowCardExistsDialogDismiss
                    )
                            .apply { setCancelable(false) }
                            .show()
                }.addTo(disposables)
    }
}
