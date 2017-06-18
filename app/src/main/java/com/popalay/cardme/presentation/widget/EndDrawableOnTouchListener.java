package com.popalay.cardme.presentation.widget;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public abstract class EndDrawableOnTouchListener implements View.OnTouchListener {

    private static final int DRAWABLE_START = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_END = 2;
    private static final int DRAWABLE_BOTTOM = 3;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!(view instanceof TextView)) {
                throw new IllegalArgumentException("View must extends TextView");
            }
            final int offset = (view.getRight() - ((TextView) view).getCompoundDrawables()[DRAWABLE_END]
                    .getBounds().width());
            if (event.getRawX() >= offset) {
                return onDrawableTouch(event);
            }
        }
        return false;
    }

    public abstract boolean onDrawableTouch(final MotionEvent event);

}