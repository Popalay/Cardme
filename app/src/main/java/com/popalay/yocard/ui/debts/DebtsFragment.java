package com.popalay.yocard.ui.debts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.FragmentDebtsBinding;
import com.popalay.yocard.ui.base.BaseFragment;

public class DebtsFragment extends BaseFragment {

    //@InjectPresenter CardsPresenter presenter;

    private FragmentDebtsBinding b;

    public static DebtsFragment newInstance() {
        return new DebtsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_debts, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void showAddDialog() {
        final View dialogView = View.inflate(getActivity(), R.layout.dialog_add_debt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> revealShow(dialogView, true, null));
        dialogView.findViewById(R.id.btn_save).setOnClickListener(v -> {
            revealShow(dialogView, false, dialog);
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void revealShow(View rootView, boolean reveal, final AlertDialog dialog) {
        final View view = rootView.findViewById(R.id.reveal_view);
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if (reveal) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, 0, maxRadius);
            b.buttonWrite.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.white));
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(300L);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);
            anim.setDuration(300L);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    b.buttonWrite.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.accent));
                    view.setVisibility(View.INVISIBLE);
                    startButtonTransition(b.buttonWrite);

                }
            });

            anim.start();
        }
    }

    private void initUI() {
        b.buttonWrite.setOnClickListener(this::startButtonTransition);
    }

    private void startButtonTransition(View v) {
        final Transition transition = new ChangeBounds();
        transition.setPathMotion(new ArcMotion());
        transition.setDuration(300L);
        transition.setInterpolator(new AccelerateDecelerateInterpolator());

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) v.getLayoutParams();

        final boolean isReturn = params.gravity == Gravity.CENTER;

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (!isReturn) {
                    showAddDialog();
                }
                transition.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(b.container, transition);

        params.topMargin = isReturn ? 0 : 200;
        params.gravity = isReturn ? (Gravity.END | Gravity.BOTTOM) : Gravity.CENTER;
        v.setLayoutParams(params);
    }
}
