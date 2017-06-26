package com.popalay.cardme.presentation.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.popalay.cardme.utils.StringUtils;

public class LettersImageView extends AppCompatImageView {

    private String currentText;

    public LettersImageView(Context context) {
        super(context);
    }

    public LettersImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LettersImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        if(TextUtils.equals(text, currentText)) return;
        currentText = text;
        applyLetters(text);
    }

    private void applyLetters(String text) {
        final ColorGenerator generator = ColorGenerator.MATERIAL;
        final TextDrawable drawable = TextDrawable.builder()
                .buildRound(StringUtils.getFirstLetters(text), generator.getColor(text));
        setImageDrawable(drawable);
    }
}
