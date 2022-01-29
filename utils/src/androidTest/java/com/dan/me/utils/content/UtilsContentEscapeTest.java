/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

@SuppressWarnings("SameParameterValue")
public class UtilsContentEscapeTest {

    private static final String ESCAPE = com.dan.me.utils.content.UtilsContentConstants.ESCAPE;
    private static final String POUND_ORIGIN = com.dan.me.utils.content.UtilsContentConstants.POUND_ORIGIN;
    private static final String SPLASH_ORIGIN = com.dan.me.utils.content.UtilsContentConstants.SPLASH_ORIGIN;
    private static final char SPLIT_CHAR = com.dan.me.utils.content.UtilsContentConstants.SPLIT_CHAR;

    @Test
    public void escape() {
        String contentOrigin = "hello dan";
        escape(contentOrigin, POUND_ORIGIN);
        escape(contentOrigin, SPLASH_ORIGIN);
    }

    @Test
    public void unescape() {
        final String contentOrigin = "hello dan";
        unescape(contentOrigin, POUND_ORIGIN);
        unescape(contentOrigin, SPLASH_ORIGIN);
    }

    @Test
    public void split() {
        String contentOrigin = "hello dan";
        splitSymbol(contentOrigin, "");
        splitSymbol(contentOrigin, POUND_ORIGIN);
        splitSymbol(contentOrigin, SPLASH_ORIGIN);

        contentOrigin = "";
        splitSymbol(contentOrigin, "");
        splitSymbol(contentOrigin, POUND_ORIGIN);
        splitSymbol(contentOrigin, SPLASH_ORIGIN);

        contentOrigin = "hello dan";
        splitIllegalArgumentException(SPLASH_ORIGIN);
        splitIllegalArgumentException(SPLASH_ORIGIN + contentOrigin);
        splitIllegalArgumentException(contentOrigin + SPLASH_ORIGIN);

        contentOrigin = "";
        splitIllegalArgumentException(SPLASH_ORIGIN);
        splitIllegalArgumentException(SPLASH_ORIGIN + contentOrigin);
        splitIllegalArgumentException(contentOrigin + SPLASH_ORIGIN);

        contentOrigin = "hello dan";
        splitMultiple(contentOrigin, "");
        splitMultiple(contentOrigin, POUND_ORIGIN);
        splitMultiple(contentOrigin, SPLASH_ORIGIN);
    }

    private void escape(final String contentOrigin, final String escapeSymbol) {
        // hello dan
        String content = contentOrigin;
        String contentEscaped = contentOrigin;
        String escape = UtilsContentEscape.escape(content);
        assertEquals(contentEscaped, escape);
        // #hello dan
        content = escapeSymbol + contentOrigin;
        contentEscaped = ESCAPE + escapeSymbol + contentOrigin;
        escape = UtilsContentEscape.escape(content);
        assertEquals(contentEscaped, escape);
        // hello dan#
        content = contentOrigin + escapeSymbol;
        contentEscaped = contentOrigin + ESCAPE + escapeSymbol;
        escape = UtilsContentEscape.escape(content);
        assertEquals(contentEscaped, escape);
        // hello dan#hello dan
        content = contentOrigin + escapeSymbol + contentOrigin;
        contentEscaped = contentOrigin + ESCAPE + escapeSymbol + contentOrigin;
        escape = UtilsContentEscape.escape(content);
        assertEquals(contentEscaped, escape);
    }

    private void unescape(final String contentOrigin, final String escapeSymbol) {
        // hello dan
        String content = contentOrigin;
        String escaped = UtilsContentEscape.escape(content);
        String contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
        // #hello dan
        content = escapeSymbol + contentOrigin;
        escaped = UtilsContentEscape.escape(content);
        contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
        // ##hello dan
        content = escapeSymbol + escapeSymbol + contentOrigin;
        escaped = UtilsContentEscape.escape(content);
        contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
        // hello dan#
        content =  contentOrigin + escapeSymbol;
        escaped = UtilsContentEscape.escape(content);
        contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
        // hello dan##
        content =  contentOrigin + escapeSymbol + escapeSymbol;
        escaped = UtilsContentEscape.escape(content);
        contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
        // hello dan#hello dan
        content =  contentOrigin + escapeSymbol + contentOrigin;
        escaped = UtilsContentEscape.escape(content);
        contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
        // hello dan##hello dan
        content =  contentOrigin + escapeSymbol + escapeSymbol + contentOrigin;
        escaped = UtilsContentEscape.escape(content);
        contentUnescaped = UtilsContentEscape.unescape(escaped);
        assertEquals(content, contentUnescaped);
    }

    private void splitSymbol(final String contentOrigin, final String escapeSymbol) {
        // hello dan
        String content = contentOrigin;
        String contentEscaped = UtilsContentEscape.escape(content);
        String contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        String[] expect = new String[]{content};
        String[] result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
        // #hello dan
        content = escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
        // ##hello dan
        content = escapeSymbol + escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
        // hello dan#
        content = contentOrigin + escapeSymbol;
        contentEscaped = UtilsContentEscape.escape(content);
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
        // hello dan##
        content = contentOrigin + escapeSymbol + escapeSymbol;
        contentEscaped = UtilsContentEscape.escape(content);
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
        // hello dan#hello dan
        content =  contentOrigin + escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
        // hello dan##hello dan
        content =  contentOrigin + escapeSymbol + escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
    }

    private void splitIllegalArgumentException(String content) {
        final AtomicBoolean exception = new AtomicBoolean(false);
        try {
            UtilsContentEscape.split(content);
        } catch (IllegalArgumentException illegalArgumentException) {
            exception.set(true);
        }
        assertTrue(exception.get());
    }

    private void splitMultiple(final String contentOrigin, final String escapeSymbol) {
        // hello dan
        String content = contentOrigin;
        String contentEscaped = UtilsContentEscape.escape(content);
        // /hello dan/hello dan/
        String contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        String[] expect = new String[]{content, content};
        String[] result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);

        // #hello dan
        content = escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        // /#hello dan/#hello dan/
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content, content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);

        // ##hello dan
        content = escapeSymbol + escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        // /##hello dan/##hello dan/
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content, content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);

        // hello dan#
        content = contentOrigin + escapeSymbol;
        contentEscaped = UtilsContentEscape.escape(content);
        // /hello dan#/hello dan#/
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content, content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);

        // hello dan##
        content = contentOrigin + escapeSymbol + escapeSymbol;
        contentEscaped = UtilsContentEscape.escape(content);
        // /hello dan##/hello dan##/
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content, content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);

        // hello dan#hello dan
        content =  contentOrigin + escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        // /hello dan#hello dan/hello dan#hello dan/
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content, content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);

        // hello dan##hello dan
        content =  contentOrigin + escapeSymbol + escapeSymbol + contentOrigin;
        contentEscaped = UtilsContentEscape.escape(content);
        // /hello dan##hello dan/hello dan##hello dan/
        contentSplits = SPLIT_CHAR + contentEscaped + SPLIT_CHAR + contentEscaped + SPLIT_CHAR;
        expect = new String[]{content, content};
        result = UtilsContentEscape.split(contentSplits);
        assertArrayEquals(expect, result);
    }
}