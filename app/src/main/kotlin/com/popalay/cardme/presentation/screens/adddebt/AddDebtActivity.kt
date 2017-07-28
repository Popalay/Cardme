package com.popalay.cardme.presentation.screens.adddebt

import android.animation.Animator
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddDebtBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.animation.EndAnimatorListener
import com.popalay.cardme.utils.transitions.FabTransform
import javax.inject.Inject

class AddDebtActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddDebtBinding

    override var navigator = object : CustomNavigator(this) {

        override fun exit() = this@AddDebtActivity.exitWithAnimation()

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddDebtBinding>(this, R.layout.activity_add_debt)
        FabTransform.setup(this, b.container)

        b.vm = ViewModelProviders.of(this, factory).get(AddDebtViewModel::class.java)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        b.buttonSave.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(DURATION_SHORT)
                .start()
    }

    override fun onBackPressed() = exitWithAnimation()

    private fun exitWithAnimation() {
        runOnUiThread {
            b.buttonSave.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(DURATION_SHORT)
                    .setListener(object : EndAnimatorListener {
                        override fun onAnimationEnd(animator: Animator) {
                            setResult(Activity.RESULT_CANCELED)
                            finishAfterTransition()
                        }
                    })
                    .start()
        }
    }
}
