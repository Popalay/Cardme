package com.popalay.yocard.ui.debts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private void initUI() {
        b.buttonWrite.setOnClickListener(v -> {
            AddDebtDialog dialog = AddDebtDialog.newInstance();
            dialog.attachToButton(b.buttonWrite);
            dialog.show(getFragmentManager(), null);
        });
    }
}
