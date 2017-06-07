package com.popalay.cardme.ui.base.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.popalay.cardme.utils.PatternBackgroundUtils;

public class CreditCardView extends RelativeLayout {

    public CreditCardView(Context context) {
        super(context);
        init(context);
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CreditCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = this.getMeasuredWidth();
        final int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        final int heightSpec = MeasureSpec.makeMeasureSpec((int) (width / 1.6f), MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }

    private void init(Context context) {
        setBackground(PatternBackgroundUtils.generateBackground(context));
    }
}