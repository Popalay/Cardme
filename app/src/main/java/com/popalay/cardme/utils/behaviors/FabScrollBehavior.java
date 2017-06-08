package com.popalay.cardme.utils.behaviors;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class FabScrollBehavior extends SnackbarFabBehavior {

    private Handler handler;

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, final FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if (handler == null) handler = new Handler();
        handler.postDelayed(() -> show(child), 500L);
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout,
            FloatingActionButton child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        hide(child);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
            FloatingActionButton child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    private void show(FloatingActionButton child) {
        child.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(200L)
                .start();
    }

    private void hide(FloatingActionButton child) {
        child.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(200L)
                .start();
    }
}