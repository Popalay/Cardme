package com.popalay.cardme.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.text.TextPaint
import android.util.AttributeSet
import com.popalay.cardme.R

class SettingTextView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private var settingText: String? = null
    private val textPaint: TextPaint by lazy {
        val lazyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        lazyPaint.color = ContextCompat.getColor(context, R.color.accent)
        lazyPaint.textSize = textSize
        lazyPaint
    }

    init {
        attrs?.let {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SettingTextView, defStyleAttr, 0)
            try {
                settingText = a.getString(R.styleable.SettingTextView_settingText)
            } finally {
                a.recycle()
            }
        }
    }

    fun setSettingText(settingText: String?) {
        this.settingText = settingText
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        settingText?.let {
            canvas.save()
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            val width = canvas.width
                    .minus(textPaint.measureText(settingText))
                    .minus(paddingEnd)
                    .minus(paddingStart)
            val height = -textPaint.ascent() + textPaint.descent()
            canvas.drawText(settingText, width, height, textPaint)
            canvas.restore()
        }
    }
}
