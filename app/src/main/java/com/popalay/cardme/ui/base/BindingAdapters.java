package com.popalay.cardme.ui.base;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.popalay.cardme.ui.base.widgets.OnOneOffClickListener;

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

    @BindingAdapter("bind:listPlaceholder")
    public static void setListPlaceholder(View view, List list) {
        view.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("bind:onClickDebounced")
    public static void setDebouncedOnClickListener(View view, ActionN action) {
        view.setOnClickListener(new OnOneOffClickListener(){
            @Override
            public void onSingleClick(View v) {
                action.call();
            }
        });
    }
}
