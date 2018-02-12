package com.popalay.cardme.screens.adddebt

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.DEBOUNCE_DELAY_MS
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.base.mvi.MviView
import com.popalay.cardme.base.mvi.MviViewModel
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.screens.stringAdapter
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.onEnd
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.utils.transitions.FabTransform
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddDebtActivity : BaseActivity(), MviView<AddDebtViewState, AddDebtIntent> {

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

    private lateinit var viewModel: AddDebtViewModel
    private lateinit var lastState: AddDebtViewState

    override val intents: Observable<AddDebtIntent>
        get() = Observable.merge(
            getInitialIntent(),
            getInformationChangedIntent(),
            getNameChangedIntent(),
            getAcceptIntent()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)
        initUi()

        viewModel = getViewModel<AddDebtViewModel>(factory).also { bind(it) }
    }

    override fun onBackPressed() = exitWithAnimation()

    fun exitWithAnimation() {
        buttonSave.hideAnimated { supportFinishAfterTransition() }
    }

    private fun bind(viewModel: MviViewModel<AddDebtIntent, AddDebtViewState>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun accept(state: AddDebtViewState) {
        lastState = state
        with(state) {
            Log.w("AddCardState", error)
            with(debt) {
                inputTo.setTextIfNeeded(holderName)
                inputMessage.setTextIfNeeded(message)
            }
            inputTo.stringAdapter(holderNames)
            buttonSave.isEnabled = canSave
        }
    }

    private fun bind(viewModel: AddDebtViewModel) {
        viewModel.states.bindToLifecycle().subscribe(this)
        intents.bindToLifecycle().subscribe(viewModel)
    }

    private fun getInitialIntent() = Observable.just(AddDebtIntent.Initial.GetHolderNames)

    private fun getNameChangedIntent() = RxTextView.afterTextChangeEvents(inputTo)
        .skipInitialValue()
        .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
        .map { it.editable().toString() }
        .distinctUntilChanged()
        .map { lastState.debt.copy(holderName = it) }
        .map(AddDebtIntent::DebtHolderNameChanged)

    private fun getInformationChangedIntent() = RxTextView.afterTextChangeEvents(inputMessage)
        .skipInitialValue()
        .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
        .map { it.editable().toString() }
        .distinctUntilChanged()
        .map { lastState.debt.copy(message = it) }
        .map(AddDebtIntent::DebtInformationChanged)

    private fun getAcceptIntent() = RxView.clicks(buttonSave)
        .applyThrottling()
        .map { lastState.debt }
        .map(AddDebtIntent::Accept)

    private fun initUi() {
        FabTransform.setup(this, container)
        window.sharedElementEnterTransition.onEnd { buttonSave.showAnimated() }
        layoutRoot.setOnClickListener { router.exit() }
    }
}
