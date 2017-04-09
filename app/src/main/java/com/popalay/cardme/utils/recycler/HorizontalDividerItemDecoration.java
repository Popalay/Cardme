package com.popalay.cardme.utils.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalDividerItemDecoration extends android.support.v7.widget.DividerItemDecoration {

    private final int leftOffset;
    private final int rightOffset;
    private final int size;

    private final Rect bounds = new Rect();
    private final Paint paint;

    public HorizontalDividerItemDecoration(Context context, @ColorRes int color,
            int size, int leftOffset, int rightOffset) {
        super(context, VERTICAL);
        this.size = size;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, color));
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

}
