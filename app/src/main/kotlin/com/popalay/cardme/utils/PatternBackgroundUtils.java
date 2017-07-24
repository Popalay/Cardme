package com.popalay.cardme.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.popalay.cardme.R;

import java.util.Random;

public final class PatternBackgroundUtils {

    private PatternBackgroundUtils() {
    }

    public static Drawable generateBackground(Context context, long seed, boolean withImage) {
        final int random = new Random(seed).nextInt(Integer.MAX_VALUE);
        final Drawable background = ContextCompat.getDrawable(context, getRandomBackground(random));
        if (!withImage) {
            return background;
        } else {
            final Drawable pattern = ContextCompat.getDrawable(context, getRandomPattern(random));
            background.setAlpha(140);
            return new LayerDrawable(new Drawable[] {pattern, background});
        }
    }

    @DrawableRes private static int getRandomPattern(int random) {
        final int[] patterns = new int[] {
                R.drawable.pattern_1,
                R.drawable.pattern_2,
                R.drawable.pattern_3,
        };
        return patterns[random % patterns.length];
    }

    @DrawableRes private static int getRandomBackground(int random) {
        final int[] backgrounds = new int[] {
                R.drawable.bg_card_purple,
                R.drawable.bg_card_green,
                R.drawable.bg_card_red,
                R.drawable.bg_card_grey
        };
        return backgrounds[random % backgrounds.length];
    }
}
