package com.popalay.cardme.presentation.base;

import android.databinding.BindingAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.popalay.cardme.presentation.widget.OnOneOffClickListener;

import java.util.List;

import rx.functions.ActionN;

public class BindingAdapters {

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("android:background")
    public static void setBackgroundResource(View view, int resource) {
        view.setBackgroundResource(resource);
    }

    @BindingAdapter("listPlaceholder")
    public static void setListPlaceholder(View view, List list) {
        view.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("onClickDebounced")
    public static void setDebouncedOnClickListener(View view, ActionN action) {
        view.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                action.call();
            }
        });
    }

    @BindingAdapter("hasFixedSize")
    public static void setHasFixedSize(RecyclerView view, boolean hasFixedSize) {
        view.setHasFixedSize(hasFixedSize);
    }

    @BindingAdapter("defaultList")
    public static void setDefaultList(RecyclerView view, boolean defaultList) {
        if (!defaultList) return;
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setItemAnimator(new DefaultItemAnimator());
    }
}
