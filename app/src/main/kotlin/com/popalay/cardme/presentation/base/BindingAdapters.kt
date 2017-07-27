package com.popalay.cardme.presentation.base

/*
object BindingAdapters {

    @BindingAdapter("android:src")
    fun ImageView.setImageResource(resource: Int) {
        setImageResource(resource)
    }

    @BindingAdapter("android:background")
    fun View.setBackgroundResource(resource: Int) {
        setBackgroundResource(resource)
    }

    @BindingAdapter("listPlaceholder")
    fun View.setListPlaceholder(list: List<*>?) {
        visibility = if (list == null || list.isEmpty()) View.VISIBLE else View.GONE
    }

    @BindingAdapter("onClickDebounced")
    fun View.setDebouncedOnClickListener(action: () -> Unit) {
        setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                action()
            }
        })
    }

    @BindingAdapter("hasFixedSize")
    fun RecyclerView.setHasFixedSize(hasFixedSize: Boolean) {
        setHasFixedSize(hasFixedSize)
    }

    @BindingAdapter("defaultList")
    fun RecyclerView.setDefaultList(defaultList: Boolean) {
        if (!defaultList) return
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        itemAnimator = DefaultItemAnimator()
    }

    @BindingAdapter("applyDivider")
    fun RecyclerView.applyDivider(orientation: Int) {
        if (orientation == OrientationHelper.VERTICAL) {
            addItemDecoration(HorizontalDividerItemDecoration.Builder(context)
                    .color(R.color.grey)
                    .size(R.dimen.list_divider_size)
                    .leftOffset(R.dimen.title_offset)
                    .rightOffset(R.dimen.zero_offset)
                    .build())
        }
    }

    @BindingAdapter("snap")
    fun RecyclerView.snap(apply: Boolean) {
        if (!apply) return
        PagerSnapHelper().attachToRecyclerView(this)
    }

}*/
