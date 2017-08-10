package com.popalay.cardme.utils.behaviors

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton

open class SnackbarFabBehavior(context: Context,
                               attrs: AttributeSet
) : CoordinatorLayout.Behavior<ImageButton>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ImageButton, dependency: View?): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ImageButton, dependency: View?): Boolean {
        val translationY = Math.min(0f, dependency!!.translationY - dependency.height)
        child.translationY = translationY
        return true
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: ImageButton, dependency: View?) {
        super.onDependentViewRemoved(parent, child, dependency)
        val translationY = Math.min(0, parent.bottom - child.bottom).toFloat()
        child.translationY = translationY
    }
}