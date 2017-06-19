package com.popalay.cardme.utils.recycler;

import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    @Nullable private final SwipeCallback swipeCallback;
    @Nullable private final DragCallback dragCallback;

    private boolean orderChanged;

    public SimpleItemTouchHelperCallback(@Nullable SwipeCallback swipeCallback) {
        this.swipeCallback = swipeCallback;
        this.dragCallback = null;
    }

    public SimpleItemTouchHelperCallback(@Nullable DragCallback dragCallback) {
        this.dragCallback = dragCallback;
        this.swipeCallback = null;
    }

    public SimpleItemTouchHelperCallback(@Nullable SwipeCallback swipeCallback,
            @Nullable DragCallback dragCallback) {
        this.swipeCallback = swipeCallback;
        this.dragCallback = dragCallback;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int dragFlags = 0;
            if (dragCallback != null && recyclerView.getChildCount() > 1) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            int dragFlags = 0;
            if (dragCallback != null && recyclerView.getChildCount() > 1) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            int swipeFlags = 0;
            if (swipeCallback != null) {
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
        if (recyclerView.getChildCount() == 0) return false;
        final int from = viewHolder.getAdapterPosition();
        final int to = target.getAdapterPosition();
        if (dragCallback == null || from == to) {
            return false;
        }
        dragCallback.onDragged(from, to);
        orderChanged = true;
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (swipeCallback == null) {
            return;
        }
        swipeCallback.onSwiped(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha = 1.0f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE &&
                actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder.itemView.setAlpha(0.5f);
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && orderChanged) {
            if (dragCallback == null) {
                return;
            }
            dragCallback.onDropped();
            orderChanged = false;
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1f);
    }

    public interface SwipeCallback {

        void onSwiped(int position);

    }

    public interface DragCallback {

        void onDragged(int from, int to);

        void onDropped();
    }
}
