package com.popalay.yocard.ui.holders;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.databinding.FragmentHoldersBinding;
import com.popalay.yocard.ui.base.BaseFragment;
import com.popalay.yocard.ui.base.ItemClickListener;
import com.popalay.yocard.ui.holderdetails.HolderDetailsActivity;

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
    public void openHolderCards(Holder holder) {
        startActivity(HolderDetailsActivity.getIntent(getActivity(), holder));
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
