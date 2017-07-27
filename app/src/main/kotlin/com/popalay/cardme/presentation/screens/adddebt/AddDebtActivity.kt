package com.popalay.cardme.presentation.screens.adddebt

import android.animation.Animator
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddDebtBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.animation.EndAnimatorListener
import com.popalay.cardme.utils.transitions.FabTransform
import com.popalay.cardme.utils.transitions.MorphTransform
import javax.inject.Inject

class AddDebtActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddDebtBinding

    override val navigator = object : CustomNavigator(this) {

        override fun exit() {
            //TODO implement start activity with transition command for Cicerone
            this@AddDebtActivity.exit()
        }

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddDebtBinding>(this, R.layout.activity_add_debt)

        if (!FabTransform.setup(this, b.container)) {
            MorphTransform.setup(this, b.container,
                    ContextCompat.getColor(this, R.color.dialog_background),
                    resources.getDimensionPixelSize(R.dimen.dialog_corners))
        }
        ViewModelProviders.of(this, factory).get(AddDebtViewModel::class.java).let {
            b.vm = it
        }
        initUI()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        b.buttonSave.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200L)
                .start()
    }

    override fun onBackPressed() = exit()

    fun exit() {
        setResult(Activity.RESULT_CANCELED)
        runOnUiThread {
            b.buttonSave.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(200L)
                    .setListener(object : EndAnimatorListener {
                        override fun onAnimationEnd(animator: Animator) {
                            finishAfterTransition()
                        }
                    })
                    .start()
        }
    }

    private fun initUI() {
        b.root.setOnClickListener { exit() }
    }
}
