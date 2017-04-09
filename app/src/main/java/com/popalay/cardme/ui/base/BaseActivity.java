package com.popalay.cardme.ui.base;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.popalay.cardme.R;
import com.popalay.cardme.utils.DialogFactory;
import com.popalay.cardme.utils.ViewUtil;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    private ProgressDialog mProgressDialog;

    public void showLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
            return;
        }

        mProgressDialog = DialogFactory.createProgressDialog(this, getString(R.string.please_wait));
        mProgressDialog.show();
    }

    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(String error) {
        DialogFactory.createSimpleOkErrorDialog(this, error).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        ViewUtil.hideKeyboard(this);
    }

    @Override
    public void close() {
        finish();
    }
}
