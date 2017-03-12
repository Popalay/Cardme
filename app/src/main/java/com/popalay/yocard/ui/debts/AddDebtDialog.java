package com.popalay.yocard.ui.debts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.DialogAddDebtBinding;
import com.popalay.yocard.ui.base.widgets.EndDrawableOnTouchListener;
import com.popalay.yocard.ui.base.widgets.RevealDialog;

public class AddDebtDialog extends RevealDialog {

    private static final String TAG = "AddDebtDialog";

    private DialogAddDebtBinding b;

    public static AddDebtDialog newInstance() {
        return new AddDebtDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.dialog_add_debt, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        b.buttonSave.setOnClickListener(v -> close());
        b.inputTo.setOnTouchListener(new EndDrawableOnTouchListener() {
            @Override
            public boolean onDrawableTouch(MotionEvent event) {
                close();
                return true;
            }
        });
    }
}
