package com.popalay.cardme.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import com.popalay.cardme.R
import java.util.*

object PatternBackgroundUtils {

    fun generateBackground(context: Context, seed: Long, withImage: Boolean): Drawable {
        val random = Random(seed).nextInt(Integer.MAX_VALUE)
        val background = ContextCompat.getDrawable(context, getRandomBackground(random))
        if (!withImage) {
            return background
        } else {
            val pattern = ContextCompat.getDrawable(context, getRandomPattern(random))
            background.alpha = 140
            return LayerDrawable(arrayOf(pattern, background))
        }
    }

    @DrawableRes private fun getRandomPattern(random: Int): Int {
        val patterns = intArrayOf(R.drawable.pattern_1, R.drawable.pattern_2, R.drawable.pattern_3)
        return patterns[random % patterns.size]
    }

    @DrawableRes private fun getRandomBackground(random: Int): Int {
        val backgrounds = intArrayOf(R.drawable.bg_card_purple, R.drawable.bg_card_green, R.drawable.bg_card_red, R.drawable.bg_card_blue)
        return backgrounds[random % backgrounds.size]
    }
}
