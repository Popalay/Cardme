package com.popalay.cardme.ui.base.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.popalay.cardme.R;

public class SettingTextView extends AppCompatTextView {

    private String settingText;
    private TextPaint paint;

    public SettingTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SettingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SettingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public String getSettingText() {
        return settingText;
    }

    public void setSettingText(String settingText) {
        this.settingText = settingText;
        invalidate();
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        final float width = canvas.getWidth() - paint.measureText(settingText)
                - getPaddingEnd() - getPaddingStart();
        final float height = -paint.ascent() + paint.descent();
        canvas.drawText(settingText, width, height, paint);
        canvas.restore();
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            final TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.SettingTextView, defStyleAttr, 0);
            try {
                settingText = a.getString(R.styleable.SettingTextView_settingText);
            } finally {
                a.recycle();
            }
        }
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.accent));
        paint.setTextSize(getTextSize());
    }
}
