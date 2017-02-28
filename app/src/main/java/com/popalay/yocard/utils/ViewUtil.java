package com.popalay.yocard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;

public final class ViewUtil {

    public static float pxToDp(float px) {
        final float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        final float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        final InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        if (context == null) {
            return height;
        }
        final Resources resources = context.getResources();
        final int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = resources.getDimensionPixelSize(resId);
        }
        return height;
    }
}
