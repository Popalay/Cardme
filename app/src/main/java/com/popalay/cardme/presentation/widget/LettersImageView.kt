package com.popalay.cardme.presentation.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.util.AttributeSet

import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.popalay.cardme.utils.firstLetters

class LettersImageView : AppCompatImageView {

    private var currentText: String = ""
    private val generator: ColorGenerator by lazy { ColorGenerator.MATERIAL }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setText(text: String) {
        if (TextUtils.equals(text, currentText)) return
        currentText = text
        applyLetters(text)
    }

    private fun applyLetters(text: String) {
        setImageDrawable(TextDrawable.builder().buildRound(text.firstLetters(), generator.getColor(text)))
    }
}
