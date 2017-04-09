package com.popalay.cardme.utils;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.popalay.cardme.R;

public final class BrowserUtils {

    private BrowserUtils() {
    }

    public static void openLink(Context context, String url) {
        final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary));
        final CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
