package com.popalay.cardme.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.popalay.cardme.R;

public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleInfoDialog(Context context, String message,
            @Nullable DialogInterface.OnDismissListener onDismiss) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setMessage(message)
                .setOnDismissListener(onDismiss)
                .setPositiveButton(R.string.action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkCancelDialog(Context context, String message,
            @Nullable DialogInterface.OnClickListener positiveButtonClickListener,
            @Nullable DialogInterface.OnDismissListener onDismissListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setMessage(message)
                .setPositiveButton(R.string.action_ok, positiveButtonClickListener)
                .setOnDismissListener(onDismissListener)
                .setNegativeButton(R.string.cancel, null);
        return alertDialog.create();
    }

    public static Dialog createCustomButtonsDialog(Context context, String message,
            String positiveButton, String negativeButton,
            @Nullable DialogInterface.OnClickListener positiveButtonClickListener,
            @Nullable DialogInterface.OnDismissListener onDismissListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveButtonClickListener)
                .setOnDismissListener(onDismissListener)
                .setNegativeButton(negativeButton, null);
        return alertDialog.create();
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.Cardme_Dialog_Progress);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
