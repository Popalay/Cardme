package com.popalay.yocard.ui.base;

import android.app.ProgressDialog;

import com.arellomobile.mvp.MvpActivity;
import com.popalay.yocard.R;
import com.popalay.yocard.utils.DialogFactory;
import com.popalay.yocard.utils.ViewUtil;

public abstract class BaseActivity extends MvpActivity implements BaseView {

    private ProgressDialog mProgressDialog;

    public void showLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
            return;
        }

        mProgressDialog = DialogFactory.createProgressDialog(this,
                getString(R.string.please_wait));
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
