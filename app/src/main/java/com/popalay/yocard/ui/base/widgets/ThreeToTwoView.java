package com.popalay.yocard.ui.base.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ThreeToTwoView extends RelativeLayout {

    public ThreeToTwoView(Context context) {
        super(context);
    }

    public ThreeToTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeToTwoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = this.getMeasuredWidth();
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec((int) (width / 1.6f), MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }
}