package com.popalay.cardme.presentation.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.util.AttributeSet
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.popalay.cardme.utils.extensions.firstLetters

class LettersImageView(context: Context,
                       attrs: AttributeSet? = null,
                       defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private var currentText: String? = null
    private val generator: ColorGenerator by lazy { ColorGenerator.MATERIAL }

    fun setText(text: String?) {
        if (TextUtils.equals(text, currentText)) return
        currentText = text
        applyLetters(text ?: "")
    }

    private fun applyLetters(text: String) {
        setImageDrawable(TextDrawable.builder().buildRound(text.firstLetters().toUpperCase(),
                generator.getColor(text)))
    }
}
