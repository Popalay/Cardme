package com.popalay.yocard.utils.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.popalay.yocard.R;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private boolean mShowFirstDivider;
    private boolean mShowLastDivider;

    private boolean mWithHorizontal;
    private boolean mWithVertical;

    private int mOrientation = -1;

    private int mDividerSize;

    public DividerItemDecoration(Context context, AttributeSet attrs) {
        mDividerSize = (int) context.getResources().getDimension(R.dimen.normal);
    }

    public DividerItemDecoration(Context context,
            int size,
            int color,
            @LinearLayoutCompat.OrientationMode int orientation) {
        this(context, false, false,
                orientation == VERTICAL,
                orientation == HORIZONTAL);
        mDividerSize = size;
    }

    public DividerItemDecoration(Context context, boolean showFirstDivider, boolean showLastDivider,
            boolean withHorizontal, boolean withVertical) {
        this(context, null);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
        mWithHorizontal = withHorizontal;
        mWithVertical = withVertical;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION || (position == 0 && !mShowFirstDivider)) {
            return;
        }

        if (mOrientation == -1) {
            getOrientation(parent);
        }

        outRect.top = mDividerSize;
        if (mOrientation == OrientationHelper.HORIZONTAL) {
            outRect.bottom = outRect.top;
        }
        if (mWithHorizontal && mShowLastDivider && position == (state.getItemCount() - 1)) {
            outRect.bottom = outRect.top;
        }

        outRect.left = mDividerSize;
        if (mOrientation == OrientationHelper.VERTICAL) {
            outRect.right = outRect.left;
        }
        if (mWithVertical && mShowLastDivider && position == (state.getItemCount() - 1)) {
            outRect.right = outRect.left;
        }
    }

    private void getOrientation(RecyclerView parent) {
        if (mOrientation == -1) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                mOrientation = layoutManager.getOrientation();
            } else {
                throw new IllegalStateException(
                        "DividerItemDecoration can only be used with a LinearLayoutManager.");
            }
        }
    }


}