package com.popalay.cardme.presentation.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.popalay.cardme.utils.PatternBackgroundUtils;

public class CreditCardView extends CardView {

    private boolean withImage;
    private long seed;

    public CreditCardView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CreditCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setWithImage(boolean withImage) {
        if (this.withImage == withImage) return;
        this.withImage = withImage;
        updateBackground();
    }

    public void setBackgroundGeneratorSeed(long seed) {
        if (seed == 0) seed = System.nanoTime();
        this.seed = seed;
        updateBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = this.getMeasuredWidth();
        final int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        final int heightSpec = MeasureSpec.makeMeasureSpec((int) (width / 1.6f), MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }

    private void updateBackground() {
        if (getChildCount() == 0) throw new RuntimeException("Credit card view must has a least one child");
        getChildAt(0).setBackground(PatternBackgroundUtils.generateBackground(getContext(), seed, withImage));
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        //no-op
    }
}