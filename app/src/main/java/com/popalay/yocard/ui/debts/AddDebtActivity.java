package com.popalay.yocard.ui.debts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.view.View;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.ActivityAddDebtBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.transitions.FabTransform;
import com.popalay.yocard.ui.transitions.MorphTransform;
import com.popalay.yocard.utils.TransitionUtils;

public class AddDebtActivity extends BaseActivity {

    private static final String TAG = "AddDebtActivity";

    private ActivityAddDebtBinding b;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddDebtActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_add_debt);

        if (!FabTransform.setup(this, b.container)) {
            MorphTransform.setup(this, b.container,
                    ContextCompat.getColor(this, R.color.dialog_background),
                    getResources().getDimensionPixelSize(R.dimen.dialog_corners));
        }
        if (getWindow().getSharedElementEnterTransition() != null) {
            getWindow().getSharedElementEnterTransition().addListener(new TransitionUtils.TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    getWindow().getSharedElementEnterTransition().removeListener(this);
                    initUI();
                }
            });
        } else {
            initUI();
        }
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    public void dismiss(View view) {
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

    private void initUI() {
        b.buttonSave.setOnClickListener(v -> close());
    }
}
