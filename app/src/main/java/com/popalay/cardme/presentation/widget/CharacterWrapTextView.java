package com.popalay.cardme.presentation.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CharacterWrapTextView extends AppCompatTextView {

    private Paint mTestPaint;

    public CharacterWrapTextView(Context context) {
        super(context);
        initialise(context, null);
    }

    public CharacterWrapTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialise(context, attributeSet);
    }

    public CharacterWrapTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        initialise(context, attributeSet);
    }

    private void initialise(Context context, AttributeSet attributeSet) {
        mTestPaint = new Paint();
        mTestPaint.set(getPaint());
    }

    private void refitText(String text, int textWidth) {
        if (textWidth <= 0) {
            return;
        }
        final int targetWidth = textWidth - getPaddingLeft() - getPaddingRight();
        float hi = 1f;
        float lo = 0f;

        float textWidthCalculated;
        while (hi - lo > 0.1f) {
            final float size = (hi + lo) / 2;
            mTestPaint.setLetterSpacing(size);
            textWidthCalculated = mTestPaint.measureText(text, 0, text.length());
            if (textWidthCalculated >= targetWidth) {
                hi = size;
            } else {
                lo = size;
            }
        }

        if (lo == getLetterSpacing()) {
            return;
        }

        setLetterSpacing(lo);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int height = getMeasuredHeight();
        refitText(getText().toString(), parentWidth);
        this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(getText().toString(), w);
        }
    }
}