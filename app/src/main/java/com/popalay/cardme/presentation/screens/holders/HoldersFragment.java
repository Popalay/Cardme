package com.popalay.cardme.presentation.screens.holders;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.cardme.R;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.databinding.FragmentHoldersBinding;
import com.popalay.cardme.presentation.base.BaseFragment;
import com.popalay.cardme.presentation.base.ItemClickListener;
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity;

import java.util.List;

public class HoldersFragment extends BaseFragment implements HoldersView, ItemClickListener<Holder> {

    @InjectPresenter HoldersPresenter presenter;

    private FragmentHoldersBinding b;

    private HoldersViewModel viewModel;

    public static HoldersFragment newInstance() {
        return new HoldersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_holders, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    @Override
    public void setHolders(List<Holder> holders) {
        viewModel.setHolders(holders);
    }

    @Override
    public void openHolderDetails() {
        startActivity(HolderDetailsActivity.Companion.getIntent(getContext()));
    }

    @Override
    public void onItemClick(Holder item) {
        presenter.onHolderClick(item);
    }

    private void initUI() {
        viewModel = new HoldersViewModel();
        b.setModel(viewModel);
        b.setListener(this);
    }
}
