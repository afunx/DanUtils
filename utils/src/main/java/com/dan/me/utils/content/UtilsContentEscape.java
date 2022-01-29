/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 *  '#'转义为'##'
 *  '/'转义为'#/'
 */
public class UtilsContentEscape {

    private static final char ESCAPE_CHAR = com.dan.me.utils.content.UtilsContentConstants.ESCAPE_CHAR;
    private static final char SPLIT_CHAR = com.dan.me.utils.content.UtilsContentConstants.SPLIT_CHAR;

    private static final String POUND_ESCAPE = com.dan.me.utils.content.UtilsContentConstants.POUND_ESCAPE;
    private static final String POUND_ORIGIN = com.dan.me.utils.content.UtilsContentConstants.POUND_ORIGIN;
    private static final String SPLASH_ESCAPE = com.dan.me.utils.content.UtilsContentConstants.SPLASH_ESCAPE;
    private static final String SPLASH_ORIGIN = com.dan.me.utils.content.UtilsContentConstants.SPLASH_ORIGIN;


    @NonNull
    public static String escape(@NonNull String str) {
        int indexPound = str.indexOf(POUND_ORIGIN);
        int indexSplash = str.indexOf(SPLASH_ORIGIN);
        if (indexPound < 0 && indexSplash < 0) {
            // 无需转义
            return str;
        }
        int start = 0;
        StringBuilder sb = new StringBuilder();
        while (indexPound >= 0 || indexSplash >= 0) {
            if (indexPound >= 0 && (indexSplash < 0 || indexPound < indexSplash)) {
                // '#'转义
                if (indexPound > start) {
                    sb.append(str.substring(start, indexPound));
                }
                sb.append(POUND_ESCAPE);
                start = indexPound + 1;
                indexPound = str.indexOf(POUND_ORIGIN, start);
            } else {
                // '/'转义
                if (indexSplash > start) {
                    sb.append(str.substring(start, indexSplash));
                }
                sb.append(SPLASH_ESCAPE);
                start = indexSplash + 1;
                indexSplash = str.indexOf(SPLASH_ORIGIN, start);
            }
        }
        if (start < str.length()) {
            sb.append(str.substring(start));
        }
        return sb.toString();
    }

    @NonNull
    static String unescape(@NonNull String str) {
        int index = str.indexOf(ESCAPE_CHAR);
        if (index < 0) {
            // 无需反转义
            return str;
        }
        int start = 0;
        StringBuilder sb = new StringBuilder();
        while (index >= 0) {
            sb.append(str.substring(start, index));
            // ++index: 跳过ESCAPE('#')
            sb.append(str.charAt(++index));
            start = index + 1;
            // 搜索ESCAPE('#')
            index = str.indexOf(ESCAPE_CHAR, start);
        }
        if (start < str.length()) {
            sb.append(str.substring(start));
        }
        return sb.toString();
    }

    /**
     * SPLIT('/') + AAA + SPLIT('/') + BBB + SPLIT('/') + CCC + SPLIT('/')
     * 头和尾必须是SPLIT('/')，否则视为非法参数
     * 支持: SPLIT('/') + "" + SPLIT('/')
     * 但不支持: SPLIT('/')
     *
     * 使用SPLIT('/')分割字符串为字符串数组
     *
     * @param str   被分割字符串
     * @return      字符串数组
     */
    @NonNull
    public static String[] split(@NonNull String str) {
        final int length = str.length();
        int index = str.indexOf(SPLIT_CHAR);
        // 搜寻SPLIT_CHAR('/')的全部位置
        int start;
        int count;
        List<Integer> splitPosList = new ArrayList<>();
        while (index >= 0) {
            count = 0;
            if (index == 0 ) {
                // 第一个字符为SPLIT_CHAR('/')
                splitPosList.add(index);
            } else {
                int prev = index - 1;
                while (prev >= 0 && str.charAt(prev) == ESCAPE_CHAR) {
                    --prev;
                    ++count;
                }
                // count为偶数说明index并未和ESCAPE_CHAR('#')组成SPLASH_ESCAPE("#/")
                if (count % 2 == 0) {
                    // 第index字符为SPLIT_CHAR('/')
                    splitPosList.add(index);
                }
            }
            start = index + 1;
            index = str.indexOf(SPLIT_CHAR, start);
        }
        // 检查：第一个字符和最后一个字符必须为SPLIT('/')
        if (splitPosList.size() < 2 || splitPosList.get(0) != 0 || splitPosList.get(splitPosList.size() - 1) != length - 1) {
            throw new IllegalArgumentException("str: " + str + " is invalid");
        }
        final String[] result = new String[splitPosList.size() - 1];
        start = 1;
        // 根据splitPosList来填充result[]
        for (int i = 1; i < splitPosList.size(); i++) {
            if (start == splitPosList.get(i)) {
                result[i - 1] = "";
            } else {
                result[i - 1] = unescape(str.substring(start, splitPosList.get(i)));
            }
            start = splitPosList.get(i) + 1;
        }
        return result;
    }
}
