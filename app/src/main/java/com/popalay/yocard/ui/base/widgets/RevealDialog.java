package com.popalay.yocard.ui.base.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

public abstract class RevealDialog extends DialogFragment {

    private View button;
    private DialogListener listener;

    public void attachToButton(View button) {
        this.button = button;
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (listener != null) {
            listener.onBeforeShow();
        }
        startButtonTransition();
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reveal(true);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(
                    ContextCompat.getColor(getActivity(), android.R.color.transparent)));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        startButtonTransition();
    }

    @CallSuper
    protected void close() {
        reveal(false);
    }

    private void reveal(boolean start) {
        View view = getView();
        if (view == null) {
            return;
        }
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        final Animator revealAnimator;
        if (start) {
            revealAnimator = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, 0, maxRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(300L);
        } else {
            revealAnimator = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);
            revealAnimator.setDuration(300L);
            revealAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                    RevealDialog.super.dismiss();
                    if (listener != null) {
                        listener.onAfterHide();
                    }
                }
            });
        }
        revealAnimator.start();
    }

    private void startButtonTransition() {
        if (button == null) {
            return;
        }
        final Transition transition = new ChangeBounds();
        transition.setPathMotion(new ArcMotion());
        transition.setDuration(300L);
        transition.setInterpolator(new AccelerateDecelerateInterpolator());

        TransitionManager.beginDelayedTransition((ViewGroup) button.getRootView(), transition);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) button.getLayoutParams();

        final boolean isReturn = params.gravity == Gravity.CENTER;
        params.topMargin = isReturn ? 0 : 200;
        params.gravity = isReturn ? (Gravity.END | Gravity.BOTTOM) : Gravity.CENTER;
        button.setLayoutParams(params);
    }

    public interface DialogListener {

        void onBeforeShow();

        void onAfterHide();
    }
}
