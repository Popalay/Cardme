package com.popalay.cardme.screens.adddebt

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.base.mvi.BindableMviView
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.screens.setDropDownItems
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.onEnd
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.utils.transitions.FabTransform
import io.reactivex.Observable
import javax.inject.Inject

class AddDebtActivity : BaseActivity(), BindableMviView<AddDebtViewState, AddDebtIntent> {

    companion object {

        fun getIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }

    private val inputTo: AutoCompleteTextView by bindView(R.id.input_to)
    private val inputMessage: EditText by bindView(R.id.input_message)
    private val container: LinearLayout by bindView(R.id.container)
    private val buttonSave: FloatingActionButton by bindView(R.id.button_save)
    private val layoutRoot: ScrollView by bindView(R.id.layout_root)

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject override lateinit var navigator: CustomNavigator
    @Inject lateinit var router: CustomRouter

    private lateinit var lastState: AddDebtViewState

    private val initialIntent = Observable.just(AddDebtIntent.Initial.GetHolderNames)

    private val nameChangeIntents = RxTextView.textChanges(inputTo)
        .skipInitialValue()
        .map { it.toString() }
        .map { lastState.debt.copy(holderName = it) }
        .map(AddDebtIntent::DebtHolderNameChanged)

    private val informationChangeIntents = RxTextView.textChanges(inputMessage)
        .skipInitialValue()
        .map { it.toString() }
        .map { lastState.debt.copy(message = it) }
        .map(AddDebtIntent::DebtInformationChanged)

    private val saveIntents = RxView.clicks(buttonSave)
        .applyThrottling()
        .map { lastState.debt }
        .map(AddDebtIntent::Accept)

    override val intents: Observable<AddDebtIntent> = Observable.defer {
        Observable.merge(
            initialIntent,
            informationChangeIntents,
            nameChangeIntents,
            saveIntents
        )
    }

    override fun accept(state: AddDebtViewState) {
        lastState = state
        with(state) {
            with(debt) {
                inputTo.setTextIfNeeded(holderName)
                inputMessage.setTextIfNeeded(message)
            }
            inputTo.setDropDownItems(holderNames)
            buttonSave.isEnabled = canSave
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)
        initUi()

        bind(getViewModel<AddDebtViewModel>(factory))
    }

    override fun onBackPressed() = exitWithAnimation()

    fun exitWithAnimation() {
        buttonSave.hideAnimated { supportFinishAfterTransition() }
    }

    private fun initUi() {
        FabTransform.setup(this, container)
        window.sharedElementEnterTransition.onEnd { buttonSave.showAnimated() }
        layoutRoot.setOnClickListener { router.exit() }
    }
}
