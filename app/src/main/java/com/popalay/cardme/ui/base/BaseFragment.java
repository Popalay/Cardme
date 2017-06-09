package com.popalay.cardme.ui.base;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpAppCompatFragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends MvpAppCompatFragment implements BaseView {

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    @NonNull public BaseActivity getBaseActivity() {
        final Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        throw new RuntimeException("BaseActivity is null");
    }

    @Override public void onDestroyView() {
        subscriptions.clear();
        super.onDestroyView();
    }

    public void addSubscription(Subscription subscription) {
        if (subscription.isUnsubscribed()) return;
        subscriptions.add(subscription);
    }

    protected void showLoadingDialog() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showLoadingDialog();
    }

    protected void hideLoadingDialog() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideLoadingDialog();
    }

    @Override public void showError(String error) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showError(error);
    }

    @Override public void showError(@StringRes int error) {
        showError(getString(error));
    }

    @Override public void showMessage(String message) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showMessage(message);
    }

    @Override public void hideError() { }

    @Override public void hideMessage() { }

    @Override public void showProgress() {
        showLoadingDialog();
    }

    @Override public void hideProgress() {
        hideLoadingDialog();
    }

    @Override public void hideKeyboard() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideKeyboard();
    }

    @Override public void close() {
        throw new UnsupportedOperationException("You can not close the fragment");
    }
}
