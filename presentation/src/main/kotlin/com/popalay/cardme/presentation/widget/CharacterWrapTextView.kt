package com.popalay.cardme.presentation.widget

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View

class CharacterWrapTextView(context: Context,
                            attrs: AttributeSet? = null,
                            defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private val testPaint: Paint by lazy { Paint(paint) }

    private fun refitText(text: String, textWidth: Int) {
        if (textWidth <= 0) return

        val targetWidth = textWidth - paddingLeft - paddingRight
        var hi = 1f
        var lo = 0f

        while (hi - lo > 0.1f) {
            val size = (hi + lo) / 2
            testPaint.letterSpacing = size
            val textWidthCalculated = testPaint.measureText(text, 0, text.length)
            if (textWidthCalculated >= targetWidth) hi = size else lo = size
        }

        if (lo == letterSpacing) return

        letterSpacing = lo
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parentWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = measuredHeight
        refitText(text.toString(), parentWidth)
        this.setMeasuredDimension(parentWidth, height)
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        refitText(text.toString(), width)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw) refitText(text.toString(), w)
    }
}