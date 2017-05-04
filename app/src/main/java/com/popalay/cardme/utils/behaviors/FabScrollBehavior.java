package com.popalay.cardme.utils.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class FabScrollBehavior extends SnackbarFabBehavior {

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
            FloatingActionButton child,
            View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (dyConsumed > 0) {
            child.animate()
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200L)
                    .start();
        } else if (dyConsumed < 0) {
            child.animate()
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200L)
                    .start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
            FloatingActionButton child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}