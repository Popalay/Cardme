package com.popalay.yocard.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.ShareCompat;

public final class ShareUtils {

    private ShareUtils() {
    }

    public static void shareText(Activity activity, @StringRes int title, String text) {
        final Intent intent = ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(title)
                .setType("text/plain")
                .setText(text)
                .createChooserIntent();
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }
}
