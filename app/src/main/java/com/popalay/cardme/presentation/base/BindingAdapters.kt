package com.popalay.cardme.presentation.base

import android.databinding.BindingAdapter
import android.support.v7.widget.*
import android.view.View
import android.widget.ImageView
import com.popalay.cardme.R
import com.popalay.cardme.presentation.widget.OnOneOffClickListener
import com.popalay.cardme.utils.recycler.decoration.HorizontalDividerItemDecoration

object BindingAdapters {

    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, resource: Int) = imageView.setImageResource(resource)

    @BindingAdapter("android:background")
    fun setBackgroundResource(view: View, resource: Int) = view.setBackgroundResource(resource)

    @BindingAdapter("listPlaceholder")
    fun setListPlaceholder(view: View, list: List<*>?) {
        view.visibility = if (list == null || list.isEmpty()) View.VISIBLE else View.GONE
    }

    @BindingAdapter("onClickDebounced")
    fun setDebouncedOnClickListener(view: View, action: () -> Unit) {
        view.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                action()
            }
        })
    }

    @BindingAdapter("hasFixedSize")
    fun setHasFixedSize(view: RecyclerView, hasFixedSize: Boolean) {
        view.setHasFixedSize(hasFixedSize)
    }

    @BindingAdapter("defaultList")
    fun setDefaultList(view: RecyclerView, defaultList: Boolean) {
        if (!defaultList) return
        view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
    }

    @BindingAdapter("applyDivider")
    fun applyDivider(view: RecyclerView, orientation: Int) {
        if (orientation == OrientationHelper.VERTICAL) {
            view.addItemDecoration(HorizontalDividerItemDecoration.Builder(view.context)
                    .color(R.color.grey)
                    .size(R.dimen.list_divider_size)
                    .leftOffset(R.dimen.title_offset)
                    .rightOffset(R.dimen.zero_offset)
                    .build())
        }
    }

    @BindingAdapter("snap")
    fun snap(view: RecyclerView, apply: Boolean) {
        if (!apply) return
        PagerSnapHelper().attachToRecyclerView(view)
    }

}
