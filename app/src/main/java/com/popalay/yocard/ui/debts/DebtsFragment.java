package com.popalay.yocard.ui.debts;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.FragmentDebtsBinding;
import com.popalay.yocard.ui.adddebt.AddDebtActivity;
import com.popalay.yocard.ui.base.BaseFragment;
import com.popalay.yocard.ui.transitions.FabTransform;

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
            final Intent intent = AddDebtActivity.getIntent(getActivity());
            FabTransform.addExtras(intent, ContextCompat.getColor(getActivity(), R.color.accent), R.drawable.ic_write);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), b.buttonWrite,
                    getString(R.string.transition_add_debt));
            startActivity(AddDebtActivity.getIntent(getActivity()), options.toBundle());
        });
    }
}
