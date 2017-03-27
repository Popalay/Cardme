package com.popalay.yocard.ui.adddebt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.databinding.ActivityAddDebtBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.utils.TransitionUtils;
import com.popalay.yocard.utils.transitions.FabTransform;
import com.popalay.yocard.utils.transitions.MorphTransform;

import java.util.List;

public class AddDebtActivity extends BaseActivity implements AddDebtView {

    private static final String TAG = "AddDebtActivity";

    @InjectPresenter AddDebtPresenter presenter;

    private ActivityAddDebtBinding b;
    private AddDebtViewModel addDebtViewModel;

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
        close();
    }

    @Override
    public void close() {
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

    @Override
    public void setCompletedCardHolders(List<String> holders) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, holders);
        b.inputTo.setAdapter(adapter);
    }

    private void initUI() {
        addDebtViewModel = new AddDebtViewModel();
        b.setModel(addDebtViewModel);

        b.buttonSave.setOnClickListener(v -> presenter.onSaveClick(b.getModel().debt));
        b.root.setOnClickListener(v -> close());

        b.inputMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (b.buttonSave.isEnabled()) {
                    b.buttonSave.performClick();
                    return true;
                }
            }
            return false;
        });
    }
}
