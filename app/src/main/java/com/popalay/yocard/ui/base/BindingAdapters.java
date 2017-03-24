package com.popalay.yocard.ui.base;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

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
    public static void setBackgroundResource(View view, List list) {
        view.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
