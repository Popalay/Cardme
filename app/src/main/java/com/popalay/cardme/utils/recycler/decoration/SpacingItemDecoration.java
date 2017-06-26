package com.popalay.cardme.utils.recycler.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.popalay.cardme.R;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final boolean showFirstDivider;
    private final boolean showLastDivider;
    private final boolean betweenItems;
    private final boolean onSides;
    private final int dividerSize;

    private int orientation = -1;

    private SpacingItemDecoration(Builder builder) {
        showFirstDivider = builder.showFirstDivider;
        showLastDivider = builder.showLastDivider;
        betweenItems = builder.betweenItems;
        onSides = builder.onSides;
        dividerSize = builder.dividerSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        getOrientation(parent);

        if (orientation == OrientationHelper.HORIZONTAL) {
            applyToHorizontalList(outRect, position, state.getItemCount());
        } else {
            applyToVerticalList(outRect, position, state.getItemCount());
        }
    }

    private void applyToVerticalList(Rect outRect, int position, int count) {
        if (betweenItems) {
            outRect.top = dividerSize / 2;
            outRect.bottom = dividerSize / 2;
        }

        if (onSides) {
            outRect.left = dividerSize;
            outRect.right = dividerSize;
        }

        if (position == 0) {
            outRect.top = showFirstDivider ? dividerSize : 0;
        } else if (position == count - 1) {
            outRect.bottom = showLastDivider ? dividerSize : 0;
        }
    }

    private void applyToHorizontalList(Rect outRect, int position, int count) {
        if (betweenItems) {
            outRect.left = dividerSize / 2;
            outRect.right = dividerSize / 2;
        }

        if (onSides) {
            outRect.top = dividerSize;
            outRect.bottom = dividerSize;
        }

        if (position == 0) {
            outRect.left = showFirstDivider ? dividerSize : 0;
        } else if (position == count - 1) {
            outRect.right = showLastDivider ? dividerSize : 0;
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

    public static final class Builder {

        private boolean showFirstDivider;
        private boolean showLastDivider;
        private boolean betweenItems;
        private boolean onSides;
        private int dividerSize;

        public Builder(Context context) {
            this.dividerSize = (int) context.getResources().getDimension(R.dimen.normal);
        }

        public Builder dividerSize(int dividerSize) {
            this.dividerSize = dividerSize;
            return this;
        }

        public Builder firstDivider(boolean val) {
            showFirstDivider = val;
            return this;
        }

        public Builder lastDivider(boolean val) {
            showLastDivider = val;
            return this;
        }

        public Builder betweenItems(boolean val) {
            betweenItems = val;
            return this;
        }

        public Builder onSides(boolean val) {
            onSides = val;
            return this;
        }

        public SpacingItemDecoration build() {
            return new SpacingItemDecoration(this);
        }
    }
}