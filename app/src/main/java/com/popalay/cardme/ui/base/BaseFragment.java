package com.popalay.cardme.ui.base;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpAppCompatFragment;

public abstract class BaseFragment extends MvpAppCompatFragment implements BaseView {

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
    public void showMessage(String message) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showMessage(message);
    }

    @Override
    public void hideError() {

    }

    @Override
    public void hideMessage() {

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
