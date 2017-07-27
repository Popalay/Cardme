package com.popalay.cardme.utils.behaviors;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

public class FabScrollBehavior extends SnackbarFabBehavior {

    private Handler handler;

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, final ImageButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if (handler == null) handler = new Handler();
        handler.postDelayed(() -> show(child), 500L);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
            ImageButton child,
            View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if(dyConsumed == 0) return;
        hide(child);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
            ImageButton child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    private void show(ImageButton child) {
        child.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(200L)
                .start();
    }

    private void hide(ImageButton child) {
        child.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(200L)
                .start();
    }
}