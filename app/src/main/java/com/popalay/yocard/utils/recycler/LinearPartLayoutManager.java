package com.popalay.yocard.utils.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class LinearPartLayoutManager extends LinearLayoutManager {

    private static final float VIEW_PERCENT = 0.75f;

    public LinearPartLayoutManager(Context context) {
        super(context);
    }

    public LinearPartLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
    }

    public LinearPartLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearPartLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        fillDown(recycler);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);
    }

    private void fillDown(RecyclerView.Recycler recycler) {
        int pos = 0;
        boolean fillDown = true;
        final int height = getHeight();
        int viewLeft = 0;
        final int itemCount = getItemCount();
        final int viewWidth = (int) (getWidth() * VIEW_PERCENT);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(getHeight(), View.MeasureSpec.EXACTLY);

        while (fillDown && pos < itemCount) {
            final View view = recycler.getViewForPosition(pos);
            addView(view);
            measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec);
            final int decoratedMeasuredHeight = getDecoratedMeasuredHeight(view);
            layoutDecorated(view, viewLeft, 0, viewLeft + viewWidth, decoratedMeasuredHeight);
            viewLeft = getDecoratedLeft(view);
            fillDown = viewLeft <= height;
            pos++;
        }
    }

    private void measureChildWithDecorationsAndMargin(View child, int widthSpec, int heightSpec) {
        final Rect decorRect = new Rect();
        calculateItemDecorationsForChild(child, decorRect);
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        widthSpec = updateSpecWithExtra(widthSpec, lp.leftMargin + decorRect.left,
                lp.rightMargin + decorRect.right);
        heightSpec = updateSpecWithExtra(heightSpec, lp.topMargin + decorRect.top,
                lp.bottomMargin + decorRect.bottom);
        child.measure(widthSpec, heightSpec);
    }

    private int updateSpecWithExtra(int spec, int startInset, int endInset) {
        if (startInset == 0 && endInset == 0) {
            return spec;
        }
        final int mode = View.MeasureSpec.getMode(spec);
        if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(spec) - startInset - endInset, mode);
        }
        return spec;
    }
}
