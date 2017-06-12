package com.popalay.cardme.ui.screens.adddebt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.cardme.R;
import com.popalay.cardme.databinding.ActivityAddDebtBinding;
import com.popalay.cardme.ui.base.BaseActivity;
import com.popalay.cardme.utils.transitions.FabTransform;
import com.popalay.cardme.utils.transitions.MorphTransform;

import java.util.List;

public class AddDebtActivity extends BaseActivity implements AddDebtView {

    private static final String TAG = "AddDebtActivity";

    @InjectPresenter AddDebtPresenter presenter;

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
        initUI();
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

    @Override
    public void setViewModel(AddDebtViewModel viewModel) {
        b.setModel(viewModel);
    }

    private void initUI() {
        b.buttonSave.setOnClickListener(v -> presenter.onSaveClick(b.getModel().debt));
        b.root.setOnClickListener(v -> close());
    }
}
