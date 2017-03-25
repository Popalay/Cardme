package com.popalay.yocard.ui.debts;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.databinding.FragmentDebtsBinding;
import com.popalay.yocard.ui.adddebt.AddDebtActivity;
import com.popalay.yocard.ui.base.BaseFragment;
import com.popalay.yocard.utils.recycler.SimpleItemTouchHelperCallback;
import com.popalay.yocard.utils.transitions.FabTransform;

import java.util.List;

public class DebtsFragment extends BaseFragment implements DebtsView, SimpleItemTouchHelperCallback.SwipeCallback {

    @InjectPresenter DebtsPresenter presenter;

    private FragmentDebtsBinding b;
    private DebtsViewModel viewModel;
    
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

    @Override
    public void showAddDialog() {
        final Intent intent = AddDebtActivity.getIntent(getActivity());
        FabTransform.addExtras(intent, ContextCompat.getColor(getActivity(), R.color.accent), R.drawable.ic_write);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), b.buttonWrite,
                getString(R.string.transition_add_debt));
        startActivity(AddDebtActivity.getIntent(getActivity()), options.toBundle());
    }

    @Override
    public void setItems(List<Debt> items) {
        viewModel.setDebts(items);
    }

    @Override
    public void showRemoveUndoAction(Debt item, int position) {
        Snackbar.make(b.listDebts, R.string.debt_removed, Snackbar.LENGTH_SHORT)
                .setAction(R.string.action_undo, view -> presenter.onRemoveUndo(item, position))
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event == DISMISS_EVENT_ACTION) {
                            return;
                        }
                        presenter.onRemoveUndoActionDismissed(item, position);
                    }
                })
                .show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final Debt debt = viewModel.get(position);
        presenter.onItemSwiped(debt, position);
    }

    private void initUI() {
        viewModel = new DebtsViewModel();
        b.setModel(viewModel);
        
        b.buttonWrite.setOnClickListener(v -> presenter.onAddClick());
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this)).attachToRecyclerView(b.listDebts);
    }
}
