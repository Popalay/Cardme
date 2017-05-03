package com.popalay.cardme.utils.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.popalay.cardme.R;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private boolean showFirstDivider;
    private boolean showLastDivider;

    private boolean withHorizontal;
    private boolean withVertical;

    private int orientation = -1;

    private final int dividerSize;

    public SpacingItemDecoration(Context context, AttributeSet attrs) {
        dividerSize = (int) context.getResources().getDimension(R.dimen.normal);
    }

    public SpacingItemDecoration(Context context, boolean showFirstDivider, boolean showLastDivider,
            boolean withHorizontal, boolean withVertical) {
        this(context, null);
        this.showFirstDivider = showFirstDivider;
        this.showLastDivider = showLastDivider;
        this.withHorizontal = withHorizontal;
        this.withVertical = withVertical;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION || (position == 0 && !showFirstDivider)) {
            return;
        }

        if (orientation == -1) {
            getOrientation(parent);
        }

        if (orientation == OrientationHelper.HORIZONTAL) {
            applyToHorizontalList(outRect, position, state.getItemCount());
        } else {
            applyToVerticalList(outRect, position, state.getItemCount());
        }
    }

    private void applyToVerticalList(Rect outRect, int position, int count) {
        if (withHorizontal) {
            if (showFirstDivider && position == 0) {
                outRect.top = dividerSize;
                outRect.bottom = dividerSize / 2;
            } else if (showLastDivider && position == count - 1) {
                outRect.top = dividerSize / 2;
                outRect.bottom = dividerSize;
            } else {
                outRect.top = dividerSize / 2;
                outRect.bottom = dividerSize / 2;
            }
        }

        if (withVertical) {
            outRect.left = dividerSize;
            outRect.right = dividerSize;
        }
    }

    private void applyToHorizontalList(Rect outRect, int position, int count) {
        if (withVertical) {
            if (showFirstDivider && position == 0) {
                outRect.left = dividerSize / 2;
                outRect.right = dividerSize / 2;
            } else if (showLastDivider && position == count - 1) {
                outRect.left = dividerSize / 2;
                outRect.right = dividerSize / 2;
            } else {
                outRect.left = dividerSize / 2;
                outRect.right = dividerSize / 2;
            }
        }

        if (withHorizontal) {
            outRect.top = dividerSize;
            outRect.bottom = dividerSize;
        }
    }

    private void getOrientation(RecyclerView parent) {
        if (orientation == -1) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                orientation = layoutManager.getOrientation();
            } else {
                throw new IllegalStateException(
                        "SpacingItemDecoration can only be used with a LinearLayoutManager.");
            }
        }
    }

}