package com.popalay.cardme.utils;

import android.text.TextUtils;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

public class StringUtils {

    private StringUtils() {}

    public static String getFirstLetters(String source) {
        final String[] split = TextUtils.split(source, " ");
        final Optional<String> single = Stream.of(split)
                .map(s -> s.charAt(0)).map(String::valueOf)
                .reduce((value1, value2) -> value1 + value2);
        return single.isPresent() ? single.get() : null;
    }
}
