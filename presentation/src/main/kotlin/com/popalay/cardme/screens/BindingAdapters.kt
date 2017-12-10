package com.popalay.cardme.screens

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import com.jakewharton.rxrelay2.Relay
import com.popalay.cardme.App
import com.popalay.cardme.DURATION_UNDO_MESSAGE
import com.popalay.cardme.R
import com.popalay.cardme.utils.recycler.SimpleItemTouchHelperCallback

@BindingAdapter("listPlaceholder")
fun View.setListPlaceholder(list: List<*>?) {
    this.visibility = if (list == null || list.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("android:src")
fun ImageView.setImageResource(resource: Int) {
    this.setImageResource(resource)
}

@BindingAdapter("android:visibility")
fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("hasFixedSize")
fun RecyclerView.setHasFixedSize(hasFixedSize: Boolean) {
    this.setHasFixedSize(hasFixedSize)
}

@BindingAdapter("defaultList")
fun RecyclerView.setDefaultList(defaultList: Boolean) {
    if (!defaultList) return
    this.setHasFixedSize(true)
    this.layoutManager = LinearLayoutManager(this.context)
}

@BindingAdapter("applyDivider")
fun RecyclerView.applyDivider(orientation: Int) {
    val divider = DividerItemDecoration(this.context, orientation)
    divider.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.list_divider)!!)
    this.addItemDecoration(divider)
}

@BindingAdapter("snap")
fun RecyclerView.snap(apply: Boolean) {
    if (!apply) return
    PagerSnapHelper().attachToRecyclerView(this)
}

@BindingAdapter("backByArrow")
fun Toolbar.backByArrow(apply: Boolean) {
    if (!apply) return
    this.setNavigationOnClickListener { App.appComponent.getRouter().exit() }
}

@BindingAdapter("exitByClick")
fun View.exitByClick(apply: Boolean) {
    if (!apply) return
    this.setOnClickListener { App.appComponent.getRouter().exit() }
}

@BindingAdapter("stringAdapter")
fun AutoCompleteTextView.stringAdapter(values: List<String>) {
    val adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, values)
    this.setAdapter(adapter)
}

@BindingAdapter("onClick")
fun View.onClick(listener: Relay<Boolean>) {
    this.setOnClickListener {
        if (!it.isEnabled) return@setOnClickListener
        listener.accept(true)
    }
}

@BindingAdapter("onEditorAction")
fun EditText.onEditorAction(listener: Relay<Int>) {
    this.setOnEditorActionListener { _, actionId, _ ->
        listener.accept(actionId)
        true
    }
}

@BindingAdapter("bottomNavigationListener")
fun BottomNavigationView.bottomNavigationListener(listener: Relay<Int>) {
    this.setOnNavigationItemSelectedListener {
        return@setOnNavigationItemSelectedListener if (this.selectedItemId == it.itemId) false
        else {
            listener.accept(it.itemId); true
        }
    }
}

@BindingAdapter("drawerNavigationListener")
fun NavigationView.drawerNavigationListener(listener: Relay<Int>) {
    this.setNavigationItemSelectedListener {
        listener.accept(it.itemId)
        (this.parent as DrawerLayout).closeDrawers()
        true
    }
}

@BindingAdapter(value = ["onSwiped", "onUndoSwipe", "undoMessage", "onDragged", "onDropped", "swipeDrawable"], requireAll = false)
fun RecyclerView.setItemTouchHelper(onSwiped: Relay<Int>?, onUndoSwipe: Relay<Boolean>?, undoMessage: String?,
                                    onDragged: Relay<Pair<Int, Int>>?, onDropped: Relay<Boolean>?,
                                    swipeDrawable: Drawable?) {
    val swipeCallback = if (onSwiped == null) null
    else object : SimpleItemTouchHelperCallback.SwipeCallback {

        override fun onSwiped(position: Int) {
            onSwiped.accept(position)

            if (onUndoSwipe == null) return
            Snackbar.make(this@setItemTouchHelper, undoMessage ?: "", Snackbar.LENGTH_LONG)
                    .setDuration(DURATION_UNDO_MESSAGE.toInt())
                    .setAction(R.string.action_undo) {
                        onUndoSwipe.accept(true)
                        it.isEnabled = false
                    }
                    .show()
        }
    }
    val dragCallback = if (onDragged == null) null
    else object : SimpleItemTouchHelperCallback.DragCallback {

        override fun onDragged(from: Int, to: Int) = onDragged.accept(Pair(from, to))

        override fun onDropped() = onDropped?.accept(true) ?: Unit
    }

    ItemTouchHelper(SimpleItemTouchHelperCallback(swipeCallback, dragCallback, swipeDrawable))
            .attachToRecyclerView(this)
}
