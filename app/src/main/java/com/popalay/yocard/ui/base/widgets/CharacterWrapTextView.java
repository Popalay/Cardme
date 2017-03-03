package com.popalay.yocard.ui.base.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

public class CharacterWrapTextView extends android.support.v7.widget.AppCompatTextView {

    public CharacterWrapTextView(Context context) {
        super(context);
    }

    public CharacterWrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterWrapTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        applyLetterSpacing();
    }

    private void applyLetterSpacing() {
        final Rect bounds = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bounds);
        final int textWidth = bounds.width();
        post(() -> {
            final int width = getWidth();
            if (width == 0) {
                return;
            }
            setLetterSpacing((float) textWidth / width);
        });

    }

}