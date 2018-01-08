package com.popalay.cardme.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.popalay.cardme.utils.PatternBackgroundUtils

class CreditCardView : CardView {

    private val imageView: ImageView = ImageView(context)

    init {
        addView(imageView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    var isWithImage: Boolean = false
        set(value) {
            if (isWithImage == value) return
            field = value
            updateBackground()
        }

    var seed: Long = 0
        set(value) {
            field = if (value == 0L) System.nanoTime() else value
            updateBackground()
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setBackgroundGeneratorSeed(seed: Long) {
        this.seed = seed
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = this.measuredWidth
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec((width / 1.6f).toInt(), View.MeasureSpec.EXACTLY)
        super.onMeasure(widthSpec, heightSpec)
    }

    private fun updateBackground() {
        imageView.background = PatternBackgroundUtils.generateBackground(context, seed, isWithImage)
    }
}