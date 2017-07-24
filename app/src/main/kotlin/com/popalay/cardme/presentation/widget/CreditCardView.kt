package com.popalay.cardme.presentation.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View

import com.popalay.cardme.utils.PatternBackgroundUtils

class CreditCardView : CardView {

    var isWithImage: Boolean = false
        set(withImage) {
            if (this.isWithImage == withImage) return
            field = withImage
            updateBackground()
        }

    private var seed: Long = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setBackgroundGeneratorSeed(seed: Long) {
        this.seed = seed
        if (this.seed == 0L) this.seed = System.nanoTime()
        updateBackground()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = this.measuredWidth
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec((width / 1.6f).toInt(), View.MeasureSpec.EXACTLY)
        super.onMeasure(widthSpec, heightSpec)
    }

    private fun updateBackground() {
        if (childCount == 0) throw RuntimeException("Credit card view must has a least one child")
        getChildAt(0).background = PatternBackgroundUtils.generateBackground(context, seed, isWithImage)
    }
}