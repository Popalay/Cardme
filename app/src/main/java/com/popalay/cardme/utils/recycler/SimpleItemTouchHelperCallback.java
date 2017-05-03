package com.popalay.cardme.utils.recycler;

import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    @Nullable private final SwipeCallback swipeCallback;
    @Nullable private final DragCallback dragCallback;

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
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
        if (dragCallback == null) {
            return false;
        }
        dragCallback.onDragged(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDraw(Canvas c,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (swipeCallback == null) {
            return;
        }
        swipeCallback.onSwiped(viewHolder.getAdapterPosition());
    }

    public interface SwipeCallback {

        void onSwiped(int position);

    }

    public interface DragCallback {

        void onDragged(int from, int to);
    }
}
