package com.popalay.cardme.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import com.popalay.cardme.R

object DialogFactory {

    fun createSimpleOkErrorDialog(context: Context, message: String): Dialog {
        return AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.action_ok, null)
                .create()
    }

    fun createSimpleInfoDialog(context: Context, message: String,
                               onDismiss: (() -> Unit)? = null): Dialog {
        return AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setMessage(message)
                .setOnDismissListener { onDismiss?.invoke() }
                .setPositiveButton(R.string.action_ok, null)
                .create()
    }

    fun createSimpleOkCancelDialog(context: Context, message: String? = null,
                                   inPositive: (() -> Unit)? = null,
                                   onDismiss: (() -> Unit)? = null): Dialog {
        return createCustomButtonsDialog(context, message,
                context.getString(R.string.action_ok),
                context.getString(R.string.action_cancel),
                inPositive, onDismiss)
    }

    fun createCustomButtonsDialog(context: Context, @StringRes message: Int = 0,
                                  @StringRes positiveButton: Int = 0,
                                  @StringRes negativeButton: Int = 0,
                                  inPositive: (() -> Unit)? = null,
                                  onDismiss: (() -> Unit)? = null): Dialog {
        return createCustomButtonsDialog(context,
                if (message > 0) context.getString(message) else null,
                if (positiveButton > 0) context.getString(positiveButton) else null,
                if (negativeButton > 0) context.getString(negativeButton) else null,
                inPositive, onDismiss)
    }

    fun createCustomButtonsDialog(context: Context, message: String? = null,
                                  positiveButton: String? = null,
                                  negativeButton: String? = null,
                                  inPositive: (() -> Unit)? = null,
                                  onDismiss: (() -> Unit)? = null): Dialog {
        return AlertDialog.Builder(context, R.style.Cardme_Dialog)
                .setMessage(message)
                .setPositiveButton(positiveButton) { _, _ -> inPositive?.invoke() }
                .setOnDismissListener { onDismiss?.invoke() }
                .setNegativeButton(negativeButton, null)
                .create()
    }

    fun createProgressDialog(context: Context, @StringRes message: Int = 0): ProgressDialog {
        return createProgressDialog(context, if (message > 0) context.getString(message) else null)
    }

    fun createProgressDialog(context: Context, message: String? = null): ProgressDialog {
        return ProgressDialog(context, R.style.Cardme_Dialog_Progress).apply {
            setMessage(message)
            setCancelable(false)
        }
    }
}
