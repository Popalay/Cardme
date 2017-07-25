package com.popalay.cardme.utils.recycler

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


class SimpleItemTouchHelperCallback(
        private val swipeCallback: SwipeCallback? = null,
        private val dragCallback: DragCallback? = null,
        private val drawable: Drawable? = null
) : ItemTouchHelper.Callback() {

    private var orderChanged: Boolean = false

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (recyclerView.layoutManager is GridLayoutManager) {
            var dragFlags = 0
            if (dragCallback != null && recyclerView.childCount > 1) {
                dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            val swipeFlags = 0
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            var dragFlags = 0
            if (dragCallback != null && recyclerView.childCount > 1) {
                dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
            var swipeFlags = 0
            if (swipeCallback != null) {
                swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            }
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        if (recyclerView.childCount == 0) return false
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        if (dragCallback == null || from == to) {
            return false
        }
        dragCallback.onDragged(from, to)
        orderChanged = true
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeCallback?.onSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             dX: Float,
                             dY: Float,
                             actionState: Int,
                             isCurrentlyActive: Boolean) {
        if (!recyclerView.itemAnimator.isRunning && actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder.apply {
                itemView.translationX = dX

                drawable?.let {

                    val itemHeight = itemView.bottom - itemView.top
                    val itemWidth = itemView.right - itemView.left

                    val containerWidth = itemWidth + (recyclerView.width - itemWidth) / 2

                    val x = if (dX < 0F) itemView.right - containerWidth / 6 - drawable.intrinsicWidth / 2
                    else itemView.left + containerWidth / 6 - drawable.intrinsicWidth / 2

                    val y = (itemHeight - drawable.intrinsicHeight) / 2 + itemView.top

                    drawable.setBounds(x, y, x + drawable.intrinsicWidth, y + drawable.intrinsicHeight)
                    drawable.draw(c)
                }
            }

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder!!.itemView.alpha = 0.5f
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && orderChanged) {
            if (dragCallback == null) {
                return
            }
            dragCallback.onDropped()
            orderChanged = false
        }
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.apply {
            alpha = 1f
        }
    }

    interface SwipeCallback {

        fun onSwiped(position: Int)

    }

    interface DragCallback {

        fun onDragged(from: Int, to: Int)

        fun onDropped()
    }
}
