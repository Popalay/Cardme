package com.popalay.yocard.utils;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.app.ShareCompat;

public final class ShareUtils {

    private ShareUtils() {
    }

    public static void shareTetx(Activity activity, @StringRes int title, String text) {
        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(title)
                .setText(text)
                .startChooser();
    }
}
