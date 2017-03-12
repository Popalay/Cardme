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
import com.popalay.yocard.ui.adapters.HolderAdapterWrapper;
import com.popalay.yocard.ui.base.BaseFragment;
import com.popalay.yocard.ui.holdercards.HolderCardsActivity;
import com.popalay.yocard.utils.ViewUtil;
import com.popalay.yocard.utils.recycler.HorizontalDividerItemDecoration;

import java.util.List;

public class HoldersFragment extends BaseFragment implements HoldersView, HolderAdapterWrapper.HolderListener {

    @InjectPresenter HoldersPresenter presenter;

    private FragmentHoldersBinding b;
    private HolderAdapterWrapper adapterWrapper;

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
        adapterWrapper.setItems(holders);
    }

    @Override
    public void openHolderCards(Holder holder) {
        startActivity(HolderCardsActivity.getIntent(getActivity(), holder));
    }

    @Override
    public void onHolderClick(Holder holder) {
        presenter.onHolderClick(holder);
    }

    private void initUI() {
        adapterWrapper = new HolderAdapterWrapper(this);
        b.listHolders.addItemDecoration(new HorizontalDividerItemDecoration(getActivity(),
                R.color.grey, 1, ViewUtil.dpToPx(56), 0));
        adapterWrapper.attachToRecycler(b.listHolders);
    }
}
