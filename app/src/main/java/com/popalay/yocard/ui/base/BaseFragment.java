package com.popalay.yocard.ui.base;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpFragment;

public abstract class BaseFragment extends MvpFragment implements BaseView {

    @NonNull
    public BaseActivity getBaseActivity() {
        final Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        throw new RuntimeException("BaseActivity is null");
    }

    protected void showLoadingDialog() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showLoadingDialog();
    }

    protected void hideLoadingDialog() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideLoadingDialog();
    }

    @Override
    public void showError(String error) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showError(error);
    }

    @Override
    public void showProgress() {
        showLoadingDialog();
    }

    @Override
    public void hideProgress() {
        hideLoadingDialog();
    }

    @Override
    public void hideKeyboard() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideKeyboard();
    }

    @Override
    public void close() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.close();
    }
}
