package com.popalay.yocard.utils.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class ScrollAwareFabBehavior extends SnackbarFabBehavior {

    public ScrollAwareFabBehavior(Context context, AttributeSet attrs) {
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
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            child.animate()
                    .translationY(child.getHeight() + fab_bottomMargin)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300L)
                    .start();
        } else if (dyConsumed < 0) {
            child.animate()
                    .translationY(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300L)
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