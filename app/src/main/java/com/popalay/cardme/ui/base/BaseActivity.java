package com.popalay.cardme.ui.base;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.popalay.cardme.R;
import com.popalay.cardme.utils.DialogFactory;
import com.popalay.cardme.utils.ViewUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    private ProgressDialog mProgressDialog;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    public void showLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
            return;
        }

        mProgressDialog = DialogFactory.createProgressDialog(this, getString(R.string.please_wait));
        mProgressDialog.show();
    }

    public void addSubscription(Subscription subscription) {
        if (subscription.isUnsubscribed()) return;
        subscriptions.add(subscription);
    }

    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

    @Override public void showError(String error) {
        DialogFactory.createSimpleOkErrorDialog(this, error).show();
    }

    @Override public void showError(@StringRes int error) {
        showError(getString(error));
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        ViewUtil.hideKeyboard(this);
    }

    @Override public void close() {
        finish();
    }
}
