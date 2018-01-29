package com.popalay.cardme.screens.cards

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
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
import com.popalay.cardme.utils.extensions.makeSceneTransitionAnimation
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.utils.extensions.unsafeActivity
import com.popalay.cardme.utils.extensions.unsafeContext
import com.popalay.cardme.utils.recycler.SpacingItemDecoration
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CardsFragment : BaseFragment() {

	companion object {

		const val SCAN_REQUEST_CODE = 121

		fun newInstance() = CardsFragment()
	}

	@Inject lateinit var factory: ViewModelProvider.Factory
	@Inject lateinit var viewModelFacade: CardsViewModelFacade

	private lateinit var b: FragmentCardsBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
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

		b.listCards.findViewHolderForAdapterPosition(position).itemView.run {
			val imageBackground = findViewById<View>(R.id.card_view)
			val imageType = findViewById<View>(R.id.image_card_type)
			val textTitle = findViewById<View>(R.id.text_title)
			val textNumber = findViewById<View>(R.id.text_number)
			val textHolderName = findViewById<View>(R.id.text_holder)

			return unsafeActivity.makeSceneTransitionAnimation(
				imageBackground to ViewCompat.getTransitionName(imageBackground),
				imageType to ViewCompat.getTransitionName(imageType),
				textTitle to ViewCompat.getTransitionName(textTitle),
				textNumber to ViewCompat.getTransitionName(textNumber),
				textHolderName to ViewCompat.getTransitionName(textHolderName)
			)
		}
	}

	private fun initUI() {
		b.listCards.addItemDecoration(SpacingItemDecoration.create {
			dividerSize = resources.getDimension(R.dimen.normal).toInt()
			showBetween = true
			showOnSides = true
		})

		viewModelFacade.doOnShowCardExistsDialog()
			.subscribeBy {
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
