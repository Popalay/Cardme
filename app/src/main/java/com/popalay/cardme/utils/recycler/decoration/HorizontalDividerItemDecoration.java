package com.popalay.cardme.utils.recycler.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalDividerItemDecoration extends DividerItemDecoration {

    private final int leftOffset;
    private final int rightOffset;
    private final int size;

    private final Rect bounds = new Rect();
    private final Paint paint;

    private HorizontalDividerItemDecoration(Context context, @ColorRes int color,
            @DimenRes int size, @DimenRes int leftOffset, @DimenRes int rightOffset) {
        super(context, VERTICAL);
        this.size = context.getResources().getDimensionPixelSize(size);
        this.leftOffset = context.getResources().getDimensionPixelSize(leftOffset);
        this.rightOffset = context.getResources().getDimensionPixelSize(rightOffset);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, color));
    }

    private HorizontalDividerItemDecoration(Builder builder) {
        this(builder.context, builder.color, builder.size, builder.leftOffset, builder.rightOffset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        left += leftOffset;
        right += rightOffset;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, bounds);
            final int bottom = bounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            final int top = bottom - size;
            canvas.drawLine(left, top, right, bottom, paint);
        }
        canvas.restore();
    }

    public static final class Builder {

        private final Context context;
        @ColorRes private int color;
        @DimenRes private int leftOffset;
        @DimenRes private int rightOffset;
        @DimenRes private int size;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder color(@ColorRes int color) {
            this.color = color;
            return this;
        }

        public Builder leftOffset(@DimenRes int leftOffset) {
            this.leftOffset = leftOffset;
            return this;
        }

        public Builder rightOffset(@DimenRes int rightOffset) {
            this.rightOffset = rightOffset;
            return this;
        }

        public Builder size(@DimenRes int size) {
            this.size = size;
            return this;
        }

        public HorizontalDividerItemDecoration build() {
            return new HorizontalDividerItemDecoration(this);
        }
    }
}
