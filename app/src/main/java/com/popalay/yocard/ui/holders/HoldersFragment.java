package com.popalay.yocard.ui.holders;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.FragmentHoldersBinding;
import com.popalay.yocard.ui.base.BaseFragment;
import com.popalay.yocard.utils.recycler.DividerItemDecoration;

import java.util.List;

public class HoldersFragment extends BaseFragment implements HoldersView {

    //@InjectPresenter CardsPresenter presenter;

    private FragmentHoldersBinding b;

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
    public void setHolders(List<String> cards) {

    }

    @Override
    public void openHolderCards(String holderName) {

    }

    private void initUI() {
        b.listHolders.addItemDecoration(new DividerItemDecoration(getActivity(), true, true, true, true));
        //adapterWrapper.attachToRecycler(b.listCards);
    }
}
