package com.popalay.cardme.presentation.base;

import android.databinding.BindingAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxrelay2.PublishRelay;
import com.popalay.cardme.App;
import com.popalay.cardme.R;
import com.popalay.cardme.presentation.widget.OnOneOffClickListener;
import com.popalay.cardme.utils.recycler.SimpleItemTouchHelperCallback;
import com.popalay.cardme.utils.recycler.decoration.HorizontalDividerItemDecoration;

import java.util.List;

import io.reactivex.functions.Action;
import kotlin.Pair;

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
    public static void setDebouncedOnClickListener(View view, Action action) {
        view.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                try {
                    action.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @BindingAdapter("applyDivider")
    public static void applyDivider(RecyclerView view, int orientation) {
        if (orientation == OrientationHelper.VERTICAL) {
            view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(view.getContext())
                    .color(R.color.grey)
                    .size(R.dimen.list_divider_size)
                    .leftOffset(R.dimen.title_offset)
                    .rightOffset(R.dimen.zero_offset)
                    .build());
        }
    }

    @BindingAdapter("snap")
    public static void snap(RecyclerView view, boolean apply) {
        if (!apply) return;
        new PagerSnapHelper().attachToRecyclerView(view);
    }

    @BindingAdapter("backByArrow")
    public static void backByArrow(Toolbar toolbar, boolean use) {
        if (!use) return;
        toolbar.setNavigationOnClickListener(v -> App.Companion.getAppComponent().getRouter().exit());
    }

    @BindingAdapter("stringAdapter")
    public static void stringAdapter(AutoCompleteTextView view, List<String> values) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, values);
        view.setAdapter(adapter);
    }

    @BindingAdapter("onClick")
    public static void onClick(View view, PublishRelay<Boolean> listener) {
        view.setOnClickListener(v -> {
            if (!view.isEnabled()) return;
            listener.accept(true);
        });
    }

    @BindingAdapter("onEditorAction")
    public static void onEditorAction(EditText view, PublishRelay<Integer> listener) {
        view.setOnEditorActionListener((v, actionId, event) -> {
            listener.accept(actionId);
            return true;
        });
    }

    @BindingAdapter(value = {"onSwiped", "onDragged", "onDropped"}, requireAll = false)
    public static void setItemTouchHelper(RecyclerView view,
            PublishRelay<Integer> onSwiped,
            PublishRelay<Pair<Integer, Integer>> onDragged,
            PublishRelay<Boolean> onDropped) {
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(position -> {
            if (onSwiped == null) return;
            onSwiped.accept(position);
        }, new SimpleItemTouchHelperCallback.DragCallback() {
            @Override public void onDragged(int from, int to) {
                if (onDragged == null) return;
                onDragged.accept(new Pair<>(from, to));
            }

            @Override public void onDropped() {
                if (onDropped == null) return;
                onDropped.accept(true);
            }
        })).attachToRecyclerView(view);
    }
}