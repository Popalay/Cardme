package com.popalay.cardme.utils.behaviors

import android.content.Context
import android.os.Handler
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.showAnimated

class FabScrollBehavior(context: Context, attrs: AttributeSet) : SnackbarFabBehavior(context, attrs) {

    private val handler: Handler by lazy { Handler() }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout,
                                    child: FloatingActionButton,
                                    target: View,
                                    type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        handler.postDelayed({ child.showAnimated() }, 500L)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int,
                                type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed == 0) return
        child.hideAnimated()
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }
}