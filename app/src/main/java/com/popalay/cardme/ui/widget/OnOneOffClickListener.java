package com.popalay.cardme.ui.widget;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

public abstract class OnOneOffClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 500;

    private long mLastClickTime;

    public static boolean isViewClicked = false;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        final long currentClickTime = SystemClock.uptimeMillis();
        final long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return;
        }
        if (!isViewClicked) {
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }
        onSingleClick(v);
    }

    /**
     * This method delays simultaneous touch events of multiple views.
     */
    private void startTimer() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> isViewClicked = false, MIN_CLICK_INTERVAL);
    }

}